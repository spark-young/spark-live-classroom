package top.spark.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.constant.Direction;
import top.spark.live.constant.RequestStatus;
import top.spark.live.constant.RequestType;
import top.spark.live.constant.ScheduleConstant;
import top.spark.live.dto.LessonInfoDTO;
import top.spark.live.dto.ScheduleDayDTO;
import top.spark.live.entity.*;
import top.spark.live.mapper.*;
import top.spark.live.service.ScheduleService;
import top.spark.live.vo.CalendarVO;
import top.spark.live.vo.ScheduleDetailVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Override
    public List<CalendarVO> getCalendarAllTeacher(long tid) {
        return scheduleMapper.getCalendarAllTeacher(tid);
    }

    @Override
    public List<CalendarVO> getCalendarAllStudent(long sid) {
        return scheduleMapper.getCalendarAllStudent(sid);
    }

    @Override
    public int setLessonInfo(LessonInfoDTO lessonInfoDTO) {
        lessonInfoDTO.setClassTime(lessonInfoDTO.getClassTime().plusHours(8));

        QueryWrapper<Schedule> qwtime = new QueryWrapper<>();
        qwtime.lambda().eq(Schedule::getTid,lessonInfoDTO.getTid())
                .between(Schedule::getClassTime,lessonInfoDTO.getClassTime().minusMinutes(30).plusSeconds(1),lessonInfoDTO.getClassTime().plusMinutes(30).minusSeconds(1))
                .and(wrapper -> wrapper.ne(Schedule::getCid,lessonInfoDTO.getCid()).or().ne(Schedule::getChid,lessonInfoDTO.getChid()).or().ne(Schedule::getSid,lessonInfoDTO.getSid()));
        if(scheduleMapper.selectCount(qwtime) != 0){
            return ScheduleConstant.LESSON_TIME_REPETITION;
        }

        QueryWrapper<Schedule> qw = new QueryWrapper<>();
        qw.lambda().eq(Schedule::getCid,lessonInfoDTO.getCid()).eq(Schedule::getChid,lessonInfoDTO.getChid()).eq(Schedule::getSid,lessonInfoDTO.getSid());
        if(scheduleMapper.selectOne(qw) == null){
            Schedule schedule = new Schedule();
            schedule.setCid(lessonInfoDTO.getCid());
            schedule.setChid(lessonInfoDTO.getChid());
            schedule.setTid(lessonInfoDTO.getTid());
            schedule.setSid(lessonInfoDTO.getSid());
            schedule.setRoomId(UUID.randomUUID().toString().replaceAll("-",""));
            schedule.setClassTime(lessonInfoDTO.getClassTime());
            return scheduleMapper.insert(schedule) == 1 ? ScheduleConstant.ADD_LESSON_SUCCESS : ScheduleConstant.ADD_OR_MODIFY_ERROR;
        }else {//core
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cid",lessonInfoDTO.getCid());
            jsonObject.put("chid", lessonInfoDTO.getChid());
            jsonObject.put("courseName", courseMapper.selectById(lessonInfoDTO.getCid()).getName());
            jsonObject.put("chapterName", chapterMapper.selectById(lessonInfoDTO.getChid()).getTitle());
            jsonObject.put("changeTime", lessonInfoDTO.getClassTime());
            String paramJSON = jsonObject.toJSONString();

            QueryWrapper<Request> qw4 = new QueryWrapper<>();
            qw4.lambda().eq(Request::getTid,lessonInfoDTO.getTid()).eq(Request::getSid,lessonInfoDTO.getSid()).eq(Request::getParamJson,paramJSON).
                    eq(Request::getRid, RequestType.CHANGE_LESSON_TIME).eq(Request::getDirection, lessonInfoDTO.isTeacher()? Direction.TEACHER_TO_STUDENT : Direction.STUDENT_TO_TEACHER);
            if(requestMapper.selectOne(qw4) != null){ // 已经提交请求
                return ScheduleConstant.REQUEST_HAS_SEND;
            }

            System.out.println(JSON.toJSONString(lessonInfoDTO));
            Request request = new Request();
            request.setSid(lessonInfoDTO.getSid());
            request.setTid(lessonInfoDTO.getTid());
            request.setRid(RequestType.CHANGE_LESSON_TIME);
            request.setContent(lessonInfoDTO.getContent());
            request.setStatus(RequestStatus.NOT_SOLVE);
            request.setDirection(lessonInfoDTO.isTeacher()? Direction.TEACHER_TO_STUDENT : Direction.STUDENT_TO_TEACHER);

            request.setParamJson(jsonObject.toJSONString());

            return requestMapper.insert(request) == 1 ?  ScheduleConstant.MODIFY_LESSON_SUCCESS : ScheduleConstant.ADD_OR_MODIFY_ERROR;
        }
    }

    @Override
    public List<ScheduleDetailVO> getScheduleDetailTeacher(ScheduleDayDTO scheduleDayDTO) {
        long tid = scheduleDayDTO.getId();
        LocalDateTime day = scheduleDayDTO.getDay();
//        LocalDateTime day = LocalDateTime.of(Integer.valueOf(scheduleDayDTO.getDay().split("-")[0]),
//                Integer.valueOf(scheduleDayDTO.getDay().split("-")[1]),
//                Integer.valueOf(scheduleDayDTO.getDay().split("-")[2]),
//                0,0,0);
        String tName = userMapper.selectById(tid).getUserId();

        QueryWrapper<Schedule> qw = new QueryWrapper<>();
        qw.lambda().eq(Schedule::getTid,tid).ne(Schedule::getSid,0)
                .between(Schedule::getClassTime,day,day.plusDays(1)).orderByAsc(Schedule::getClassTime);
        List<Schedule> schedules = scheduleMapper.selectList(qw);

        System.out.println(JSON.toJSONString(schedules));

        if(schedules == null)
            return null;

        List<ScheduleDetailVO> scheduleDetailVOS = new ArrayList<>();

        schedules.forEach((e) -> {
            ScheduleDetailVO scheduleDetailVO = new ScheduleDetailVO();
            scheduleDetailVO.setCid(e.getCid());
            scheduleDetailVO.setChid(e.getChid());
            scheduleDetailVO.setSid(e.getSid());
            scheduleDetailVO.setTid(e.getTid());
            scheduleDetailVO.setRoomId(e.getRoomId());
            scheduleDetailVO.setTName(tName);
            Course course = courseMapper.selectById(e.getCid());
            scheduleDetailVO.setCourseName(course.getName());
            scheduleDetailVO.setSName(userMapper.selectById(e.getSid()).getUserId());
            scheduleDetailVO.setTypeName(categoryMapper.selectById(course.getTypeId()).getTitle());
            scheduleDetailVO.setChapterName(chapterMapper.selectById(e.getChid()).getTitle());
            scheduleDetailVO.setClassTime(e.getClassTime());
            scheduleDetailVOS.add(scheduleDetailVO);
        });
        System.out.println(JSON.toJSONString(scheduleDetailVOS));
        return scheduleDetailVOS;
    }

    @Override
    public List<ScheduleDetailVO> getScheduleDetailStudent(ScheduleDayDTO scheduleDayDTO) {
        long sid = scheduleDayDTO.getId();
        LocalDateTime day = scheduleDayDTO.getDay();
//        LocalDateTime day = LocalDateTime.of(Integer.valueOf(scheduleDayDTO.getDay().split("-")[0]),
//                Integer.valueOf(scheduleDayDTO.getDay().split("-")[1]),
//                Integer.valueOf(scheduleDayDTO.getDay().split("-")[2]),
//                0,0,0);
        String sName = userMapper.selectById(sid).getUserId();

        QueryWrapper<Schedule> qw = new QueryWrapper<>();
        qw.lambda().eq(Schedule::getSid,sid)
                .between(Schedule::getClassTime,day,day.plusDays(1)).orderByAsc(Schedule::getClassTime);
        List<Schedule> schedules = scheduleMapper.selectList(qw);

        System.out.println(JSON.toJSONString(schedules));

        if(schedules == null)
            return null;

        List<ScheduleDetailVO> scheduleDetailVOS = new ArrayList<>();

        schedules.forEach((e) -> {
            ScheduleDetailVO scheduleDetailVO = new ScheduleDetailVO();
            scheduleDetailVO.setCid(e.getCid());
            scheduleDetailVO.setChid(e.getChid());
            scheduleDetailVO.setSid(e.getSid());
            scheduleDetailVO.setTid(e.getTid());
            scheduleDetailVO.setRoomId(e.getRoomId());
            scheduleDetailVO.setTName(userMapper.selectById(e.getTid()).getUserId());
            Course course = courseMapper.selectById(e.getCid());
            scheduleDetailVO.setCourseName(course.getName());
            scheduleDetailVO.setSName(sName);
            scheduleDetailVO.setTypeName(categoryMapper.selectById(course.getTypeId()).getTitle());
            scheduleDetailVO.setChapterName(chapterMapper.selectById(e.getChid()).getTitle());
            scheduleDetailVO.setClassTime(e.getClassTime());
            scheduleDetailVOS.add(scheduleDetailVO);
        });
        System.out.println(JSON.toJSONString(scheduleDetailVOS));
        return scheduleDetailVOS;
    }

    @Override
    public int getTodayClassCount(long id) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayS = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0,0);

        QueryWrapper<Course> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(Course::getStatus,1);
        List<Course> list = courseMapper.selectList(qw2);
        List<Long> cids = new ArrayList<>();
        list.forEach((e) -> cids.add(e.getId()));

        QueryWrapper<Schedule> qw = new QueryWrapper<>();
        QueryWrapper<UserRoleRelation> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(UserRoleRelation::getUserId,id);
        if(userRoleRelationMapper.selectOne(qw1).getRoleId() == 1){//教师
            qw.lambda().between(Schedule::getClassTime,todayS,todayS.plusDays(1)).eq(Schedule::getTid,id).in(Schedule::getCid,cids);
        }else {//学生
            qw.lambda().between(Schedule::getClassTime,todayS,todayS.plusDays(1)).eq(Schedule::getSid,id).in(Schedule::getCid,cids);
        }
        return scheduleMapper.selectCount(qw);
    }

    //    QueryWrapper<Course> qwc = new QueryWrapper<>();
//        qwc.lambda().eq(Course::getTid,tid);
//    List<Course> courseList = courseMapper.selectList(qwc);
//    String teacherName = userMapper.selectById(tid).getUserId();
//
//        if(courseList != null){
//        List<Long> cids = new ArrayList<>();
//        HashMap<Long,String> cname = new HashMap<>();
//        courseList.forEach((e) -> {//构成cid到cname映射
//            cids.add(e.getId());
//            cname.put(e.getId(),e.getName());
//        });
//
//        HashMap<Long,Long> sids = new HashMap<>();
//        HashMap<Long,String> sname = new HashMap<>();
//        HashMap<Long,String> csName = new HashMap<>();
//        List<CourseSelection> courseSelectionList = courseSelectionMapper.selectBatchIds(cids);
//        if(courseSelectionList != null){//构成从cid到sName的映射
//            courseSelectionList.forEach((e) -> sids.put(e.getCid(),e.getSid()));
//            List<User> users = userMapper.selectBatchIds(sids.values());
//            users.forEach((e) -> sname.put(e.getId(),e.getUserId()));
//            courseList.forEach((e) -> csName.put(e.getId(),sname.get(sids.get(e.getId()))));
//        }
//
//        //构成cid到typeName映射
//        HashMap<Long,String> typeName = new HashMap<>();
//        List<Long> typeIds = new ArrayList<>();
//        HashMap<Long,Long> ctIds = new HashMap<>();
//        courseList.forEach((e) -> {
//            typeIds.add(e.getTypeId());
//            ctIds.put(e.getId(),e.getTypeId());
//        });
//        List<Category> categories = categoryMapper.selectBatchIds(typeIds);
//        HashMap<Long,String> cateName = new HashMap<>();
//        categories.forEach((e) -> cateName.put(e.getId(),e.getTitle()));
//        courseList.forEach((e) -> typeName.put(e.getId(),cateName.get(ctIds.get(e.getId()))));
//
//        List<Schedule> scheduleList = scheduleMapper.selectBatchIds(cids);
//
//        if(scheduleList != null){
//            HashMap
//        }
//    }
//        return null;
}
