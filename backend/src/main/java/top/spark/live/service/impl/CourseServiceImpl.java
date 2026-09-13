package top.spark.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.constant.CourseConstant;
import top.spark.live.constant.Direction;
import top.spark.live.constant.RequestStatus;
import top.spark.live.constant.RequestType;
import top.spark.live.dto.*;
import top.spark.live.entity.*;
import top.spark.live.mapper.*;
import top.spark.live.service.CourseService;
import top.spark.live.vo.ChapterVO;
import top.spark.live.vo.CourseDetailsVO;
import top.spark.live.vo.CourseListVO;
import top.spark.live.vo.LessonInfoVO;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private ChapterRootFirstMapper chapterRootFirstMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private UserMapper userMapper;

    private Lock lock = new ReentrantLock();

    @Override
    public List<CourseListVO> getCourseList() {
        List<CourseListVO> result = courseMapper.getCourseList();
        return result;
    }

    @Override
    public List<CourseListVO> getCourseListByTypeId(long typeId) {
        List<CourseListVO> result = courseMapper.getCourseListByTypeId(typeId);
        return result;
    }

    @Override
    public List<CourseListVO> getCourseListBySearch(String content) {
        List<CourseListVO> result = courseMapper.getCourseListBySearch(content);
        return result;
    }

    @Override
    public List<CourseListVO> getCourseListByTid(long tid) {
        List<CourseListVO> result = courseMapper.getCourseListByTid(tid);
        result.forEach((e) -> {
            QueryWrapper<CourseSelection> qw = new QueryWrapper();
            qw.lambda().eq(CourseSelection::getCid, e.getId());
            e.setCurCount(courseSelectionMapper.selectCount(qw));
        });
        return result;
    }

    @Override
    public List<CourseListVO> getCourseListBySid(long sid) {
        List<CourseListVO> result = courseMapper.getCourseListBySid(sid);
        return result;
    }

    @Override
    public CourseDetailsVO getCourseDetails(long courseId) {
        CourseDetailsVO result = courseMapper.getCourseDetails(courseId);

        ChapterRootFirst chapterRootFirst = chapterRootFirstMapper.selectById(courseId);
        List<Chapter> chapters = chapterMapper.getChapterByCourseId(courseId);

        /**
         * 为null说明该课程暂未创建或暂无章节信息
         */
        if (chapterRootFirst == null) {
            return result;
        }

        //list首元素为指向首层根节点的节点
        Chapter chapter = new Chapter();
        chapter.setId(-1);
        chapter.setCrid(chapterRootFirst.getChid());
        chapters.add(0, chapter);

        result.setChapterJSON(JSON.toJSONString(getChapterToList(chapters)));
        return result;
    }

    @Override
    public int subcribeCourse(long courseId, long sid) {
        Course course = courseMapper.selectById(courseId);
        QueryWrapper<CourseSelection> qw = new QueryWrapper<>();
        qw.lambda().eq(CourseSelection::getCid, courseId);
        QueryWrapper<CourseSelection> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(CourseSelection::getSid, sid).eq(CourseSelection::getCid, courseId);

        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setCid(courseId);
        courseSelection.setTid(course.getTid());
        courseSelection.setSid(sid);
        if(courseSelectionMapper.selectOne(qw1) != null){
            return CourseConstant.STUDENT_HAS_SUBCRIBE;
        }

        lock.lock();
        try{
            return insertCourseSelection(courseId, course.getSubCount(), courseSelection) ? CourseConstant.SUBCRIBE_SUCCESS : CourseConstant.CLASS_HAS_MAX_COUNT;
        } catch (Exception e){}
        finally {
            lock.unlock();
        }
        return CourseConstant.SUBCRIBE_SUCCESS;

//        if (courseSelectionMapper.selectById(courseId) != null) {
//            return false;
//        }
//        CourseSelection courseSelection = new CourseSelection();
//        Course course = courseMapper.selectById(courseId);
//        courseSelection.setCid(courseId);
//        courseSelection.setTid(course.getTid());
//        courseSelection.setSid(sid);
//        courseSelectionMapper.insert(courseSelection);
//
//        scheduleMapper.subscribeSchedule(sid,courseId);
//        return true;
    }

    /**
     * 修改学生课程
     * @param courseInfoDTO
     * @return 返回 -1 表示成功修改，否则为修改失败，返回当前订阅数量
     */
    @Override
    public int modifyCourseInfo(CourseInfoDTO courseInfoDTO) {
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.lambda().eq(Course::getId, courseInfoDTO.getId());
        Course course = courseMapper.selectById(courseInfoDTO.getId());

        QueryWrapper<CourseSelection> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(CourseSelection::getCid,courseInfoDTO.getId());

        lock.lock();

        try{
            int curCount = courseSelectionMapper.selectCount(qw1);
            if(curCount > courseInfoDTO.getSubCount()){//当前已订阅数量大于修改值，此次不做修改
                lock.unlock();
                return curCount;
            }

            course.setTypeId(courseInfoDTO.getTypeId());
            course.setName(courseInfoDTO.getName());
            course.setSubCount(courseInfoDTO.getSubCount());
            course.setDescription(courseInfoDTO.getDescription());
            courseMapper.update(course, qw);
        } catch (Exception e){}
        finally {
            lock.unlock();
        }

        return -1;
    }

    @Override
    public long addCourseInfo(CourseAddDTO courseAddDTO) {
        Course course = new Course();
        course.setTid(courseAddDTO.getTid());
        course.setTypeId(courseAddDTO.getTypeId());
        course.setName(courseAddDTO.getName());
        course.setDescription(courseAddDTO.getDescription());
        course.setSubCount(courseAddDTO.getSubCount());
        course.setUrl(courseAddDTO.getUrl());
        courseMapper.insert(course);
        return course.getId();
    }

    @Override
    public int deleteCourse(long cid) {
        QueryWrapper<CourseSelection> qw = new QueryWrapper<>();
        qw.lambda().eq(CourseSelection::getCid, cid);
        if (courseSelectionMapper.selectCount(qw) != 0) {
            return CourseConstant.DELETE_FAIL_COURSE_SELECTED;
        } else {
            QueryWrapper<Chapter> qw1 = new QueryWrapper<>();
            qw1.lambda().eq(Chapter::getCid, cid);
            try {
                courseMapper.deleteById(cid);
                chapterMapper.delete(qw1);//删除chapter表中cid
                courseSelectionMapper.deleteById(cid);//删除course_selection表中cid
                chapterRootFirstMapper.deleteById(cid);//删除chapter_root_first表中cid
                scheduleMapper.deleteById(cid);
                return CourseConstant.DELETE_COURSE_SUCCESS;
            } catch (Exception e) {
                return CourseConstant.DELETE_FAIL_UNKNOW;
            }
        }
    }

    @Override
    public boolean UnsubcribeCourse(UnsubcribeCourseDTO unsubcribeCourseDTO) {
        Course course = courseMapper.selectById(unsubcribeCourseDTO.getCid());

        JSONObject object = new JSONObject();
        object.put("sName",userMapper.selectById(unsubcribeCourseDTO.getSid()).getUserId());
        object.put("courseName",course.getName());
        object.put("sid",unsubcribeCourseDTO.getSid());
        object.put("cid",unsubcribeCourseDTO.getCid());
        String paramJson = object.toJSONString();

        QueryWrapper<Request> qw = new QueryWrapper<>();
        qw.lambda().eq(Request::getTid,course.getTid()).eq(Request::getSid,unsubcribeCourseDTO.getSid()).eq(Request::getParamJson,paramJson).
                eq(Request::getRid, RequestType.DELETE_COURSE).eq(Request::getDirection, Direction.STUDENT_TO_TEACHER);
        if(requestMapper.selectOne(qw) != null){ // 已经提交请求
            return false;
        }
        Request request = new Request();
        request.setTid(course.getTid());
        request.setSid(unsubcribeCourseDTO.getSid());
        request.setContent(unsubcribeCourseDTO.getContent());
        request.setParamJson(paramJson);
        request.setDirection(Direction.STUDENT_TO_TEACHER);
        request.setStatus(RequestStatus.NOT_SOLVE);
        request.setRid(RequestType.DELETE_COURSE);
        requestMapper.insert(request);
        return true;


//        QueryWrapper<CourseSelection> qw = new QueryWrapper<>();
//        qw.lambda().eq(CourseSelection::getSid,sid).eq(CourseSelection::getCid,cid);
//        int delete = courseSelectionMapper.delete(qw);
//        if (delete == 0) {
//            return false;
//        }
//        QueryWrapper<Schedule> qw1 = new QueryWrapper<>();
//        qw1.lambda().eq(Schedule::getCid,cid).eq(Schedule::getSid,sid);
//        scheduleMapper.delete(qw1);
//        return true;
    }

    @Override
    public List<LessonInfoVO> getLessonInfo(long chid) {
        QueryWrapper<Chapter> qw = new QueryWrapper<>();
        qw.lambda().eq(Chapter::getId, chid);
        return scheduleMapper.getLessonInfo(chapterMapper.selectOne(qw).getCid(), chid);
    }

    @Override
    public String getChapter(long cid) {
        List<ChapterVO> chapterVOs = chapterMapper.getChapterAndClassTimeByCid(cid);
        if (chapterVOs.isEmpty()) {
            return "";
        }
        ChapterRootFirst chapterRootFirst = chapterRootFirstMapper.selectById(cid);
        //list首元素为指向首层根节点的节点
        ChapterVO chapterVO = new ChapterVO();
        chapterVO.setId(-1);
        chapterVO.setCrid(chapterRootFirst.getChid());
        chapterVOs.add(0, chapterVO);

        return JSON.toJSONString(getChapterToTree(chapterVOs));
    }

    @Override
    public String insertChapterRoot(ChapterRootDTO chapterRootDTO) {
        ChapterRootFirst chapterRootFirst = chapterRootFirstMapper.selectById(chapterRootDTO.getCid());
        Chapter chapter = new Chapter();
        chapter.setCid(chapterRootDTO.getCid());
        chapter.setNid(0);
        chapter.setCrid(0);
        chapter.setTitle(chapterRootDTO.getTitle());
        chapter.setDescription(chapterRootDTO.getDescription());
        chapter.setUrl(chapterRootDTO.getUrl());
        chapterMapper.insert(chapter);
        if (chapterRootDTO.getPreChid() != 0) {
            Chapter preChapter = chapterMapper.selectById(chapterRootDTO.getPreChid());//查出前一个章节点
            preChapter.setNid(chapter.getId());
            chapterMapper.updateById(preChapter);
        }
        if (chapterRootFirst == null) {
            chapterRootFirst = new ChapterRootFirst();
            chapterRootFirst.setCid(chapter.getCid());
            chapterRootFirst.setChid(chapter.getId());
            chapterRootFirstMapper.insert(chapterRootFirst);
        }
        ChapterVO result = new ChapterVO();
        result.setId(chapter.getId());
        result.setTitle(chapter.getTitle());
        result.setDescription(chapter.getDescription());
        result.setNid(chapter.getNid());
        result.setCrid(chapter.getCrid());
        result.setSection(chapter.getSection());
        return JSON.toJSONString(result);
    }

    @Override
    public String insertChapterNode(ChapterNodeDTO chapterNodeDTO) {
        Chapter chapter = new Chapter();
        chapter.setCid(chapterNodeDTO.getCid());
        chapter.setNid(0);
        chapter.setCrid(0);
        chapter.setTitle(chapterNodeDTO.getTitle());
        chapter.setDescription(chapterNodeDTO.getDescription());
        chapter.setUrl(chapterNodeDTO.getUrl());
        chapterMapper.insert(chapter);
        if (chapterNodeDTO.getPreChid() != 0) {
            Chapter preChapter = chapterMapper.selectById(chapterNodeDTO.getPreChid());//查出前一个章节点
            preChapter.setNid(chapter.getId());
            chapterMapper.updateById(preChapter);
        } else {
            Chapter parChapter = chapterMapper.selectById(chapterNodeDTO.getParChid());//查出父章节点
            parChapter.setCrid(chapter.getId());
            chapterMapper.updateById(parChapter);
        }
        ChapterVO result = new ChapterVO();
        result.setId(chapter.getId());
        result.setTitle(chapter.getTitle());
        result.setDescription(chapter.getDescription());
        result.setNid(chapter.getNid());
        result.setCrid(chapter.getCrid());
        result.setSection(chapter.getSection());
        return JSON.toJSONString(result);
    }

    @Override
    public int deleteChapterNode(ChapterNoteDeleteDTO chapterNoteDeleteDTO) {
        Long[] chapters = chapterNoteDeleteDTO.getIds();
        Chapter chapterFirst = chapterMapper.selectById(chapters[0]);
        if (chapterNoteDeleteDTO.getPreChid() == 0) {
            //说明当前节点为最左节点
            if (chapterNoteDeleteDTO.getParChid() == 0) {
                //说明当前节点是全局左根节点，删除chapterRootFirst对应数据
                if (chapterFirst.getNid() == 0) {
                    //说明该节点没有后继邻节点
                    chapterRootFirstMapper.deleteById(chapterFirst.getCid());
                } else {
                    //说明该节点有后继邻节点，更新chapterRootFirst对应数据
                    ChapterRootFirst chapterRootFirst = new ChapterRootFirst();
                    chapterRootFirst.setCid(chapterFirst.getCid());
                    chapterRootFirst.setChid(chapterFirst.getNid());
                    chapterRootFirstMapper.updateById(chapterRootFirst);
                }
            } else {
                //说明该节点为非全局根节点的最左节点
                Chapter chapterParent = chapterMapper.selectById(chapterNoteDeleteDTO.getParChid());
                chapterParent.setCrid(chapterFirst.getNid());
                chapterMapper.updateById(chapterParent);
            }
        } else {
            // 说明该节点不是最左节点，只需将preChid的nid指向该节点的nid
            Chapter chapterPreNode = chapterMapper.selectById(chapterNoteDeleteDTO.getPreChid());
            chapterPreNode.setNid(chapterFirst.getNid());
            chapterMapper.updateById(chapterPreNode);
        }
        int count = chapterMapper.deleteBatchIds(Arrays.asList(chapters));
        return count;
    }

//    @Override
//    public int updateChapterLeafNode(ChapterLeafNodeDTO chapterLeafNodeDTO) {
//        if (updateChapterNode(chapterLeafNodeDTO) == 0) {
//            return 0;
//        }
//        QueryWrapper<Schedule> qw = new QueryWrapper<>();
//        qw.lambda().eq(Schedule::getChid, chapterLeafNodeDTO.getId());
//        Schedule schedule = scheduleMapper.selectOne(qw);
//        chapterLeafNodeDTO.setClassTime(chapterLeafNodeDTO.getClassTime().plusHours(8));//时区问题需要加上8小时
//        if (schedule == null) {
//            schedule = new Schedule();
//            schedule.setCid(chapterLeafNodeDTO.getCid());
//            schedule.setTid(courseMapper.selectById(chapterLeafNodeDTO.getCid()).getTid());
//            CourseSelection courseSelection = courseSelectionMapper.selectById(chapterLeafNodeDTO.getCid());
//            if(courseSelection != null){
//                schedule.setSid(courseSelection.getSid());
//            }
//            schedule.setChid(chapterLeafNodeDTO.getId());
//            schedule.setClassTime(chapterLeafNodeDTO.getClassTime());
//            schedule.setRoomId(UUID.randomUUID().toString().replaceAll("-", ""));
//            return scheduleMapper.insert(schedule);
//        } else {
//            schedule.setClassTime(chapterLeafNodeDTO.getClassTime());
//            QueryWrapper<Schedule> qw1 = new QueryWrapper<>();
//            qw1.lambda().eq(Schedule::getChid, chapterLeafNodeDTO.getId());
//            return scheduleMapper.update(schedule, qw1);
//        }
//    }

    @Override
    public int updateChapterNode(ChapterNodeDTO chapterNodeDTO) {
        Chapter chapter = chapterMapper.selectById(chapterNodeDTO.getId());
        chapter.setTitle(chapterNodeDTO.getTitle());
        chapter.setDescription(chapterNodeDTO.getDescription());
        chapter.setUrl(chapterNodeDTO.getUrl());
        return chapterMapper.updateById(chapter);
    }

    /**
     * 此方法基于section聚合树形结构，sort保证有序性，暂废弃使用
     * @param list
     * @return
     */
    //    public List<ChapterVO> getChapterToList(List<Chapter> list){
//        List<ChapterVO> result = new ArrayList<>(),items;
//        int sectionLevel = 0,sectionIndex;
//        for(Chapter ct : list){
//            sectionLevel = ct.getSection().split(",").length;
//            items = result;
//            if(sectionLevel != 1){
//                for(int i=0;i<sectionLevel-1;i++){
//                    sectionIndex = Integer.valueOf(ct.getSection().split(",")[i])-1;
//                    items = items.get(sectionIndex).getChildren();
//                }
//            }
//            items.add(new ChapterVO(ct.getTitle(),ct.getDescription(),ct.getSection()));
//        }
//        return result;
//    }

    /**
     * 此方法基于双指针（nid邻节点指针，crid次级元素首节点指针）聚合树形结构并保证有序性
     * 其中list的第一个元素应当是指向首层根节点的
     *
     * @param list
     * @return
     */
    private List<ChapterVO> getChapterToTree(List<ChapterVO> list) {
        Map<Long, ChapterVO> ctMap = new HashMap<>();
        List<ChapterVO> voList = new ArrayList<>();

        list.forEach((ct) -> voList.add(ct));
        voList.forEach((cvo) -> ctMap.put(cvo.getId(), cvo));

        ctMap.put(Long.valueOf(0), null);

        ChapterVO root = new ChapterVO(-1, "", "", 0, voList.get(0).getCrid(), ""), curCT, iter;
        Queue<ChapterVO> cQ = new LinkedList<>();
        cQ.offer(root);

        while (!cQ.isEmpty()) {
            curCT = cQ.poll();
            iter = ctMap.get(curCT.getCrid());
            while (iter != null) {
                curCT.getChildren().add(iter);
                cQ.offer(iter);
                iter = ctMap.get(iter.getNid());
            }
        }
        return root.getChildren();
    }

    public List<ChapterVO> getChapterToList(List<Chapter> list) {
        Map<Long, ChapterVO> ctMap = new HashMap<>();
        List<ChapterVO> voList = new ArrayList<>();

        list.forEach((ct) -> voList.add(new ChapterVO(ct.getId(), ct.getTitle(), ct.getDescription(), ct.getNid(), ct.getCrid(), ct.getSection())));
        voList.forEach((cvo) -> ctMap.put(cvo.getId(), cvo));

        ctMap.put(Long.valueOf(0), null);

        ChapterVO root = new ChapterVO(-1, "", "", 0, voList.get(0).getCrid(), ""), curCT, iter;
        Queue<ChapterVO> cQ = new LinkedList<>();
        cQ.offer(root);

        while (!cQ.isEmpty()) {
            curCT = cQ.poll();
            iter = ctMap.get(curCT.getCrid());
            while (iter != null) {
                curCT.getChildren().add(iter);
                cQ.offer(iter);
                iter = ctMap.get(iter.getNid());
            }
        }
        return root.getChildren();
    }

    private boolean insertCourseSelection(long cid, long maxCount, CourseSelection courseSelection) {
        QueryWrapper<CourseSelection> qw = new QueryWrapper<>();
        qw.lambda().eq(CourseSelection::getCid, cid);
        if (courseSelectionMapper.selectCount(qw) < maxCount) {
            courseSelectionMapper.insert(courseSelection);
            return true;
        }
        return false;
    }
}
