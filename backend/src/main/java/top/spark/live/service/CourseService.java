package top.spark.live.service;

import top.spark.live.dto.*;
import top.spark.live.entity.Chapter;
import top.spark.live.entity.Course;
import top.spark.live.vo.ChapterVO;
import top.spark.live.vo.CourseDetailsVO;
import top.spark.live.vo.CourseListVO;
import top.spark.live.vo.LessonInfoVO;

import java.util.List;

public interface CourseService {
    public List<CourseListVO> getCourseList();
    public List<CourseListVO> getCourseListByTid(long tid);
    public List<CourseListVO> getCourseListBySid(long sid);
    public List<CourseListVO> getCourseListByTypeId(long typeId);
    public List<CourseListVO> getCourseListBySearch(String content);
    public CourseDetailsVO getCourseDetails(long courseId);
    public int subcribeCourse(long courseId,long sid);

    public int modifyCourseInfo(CourseInfoDTO courseInfoDTO);
    public long addCourseInfo(CourseAddDTO courseAddDTO);
    public int deleteCourse(long cid);
    public boolean UnsubcribeCourse(UnsubcribeCourseDTO unsubcribeCourseDTO);

//    public List<ChapterVO> getChapterToList(List<Chapter> list);
    public List<LessonInfoVO> getLessonInfo(long chid);
    public String getChapter(long cid);
    public String insertChapterRoot(ChapterRootDTO chapterRootDTO);
    public String insertChapterNode(ChapterNodeDTO chapterNodeDTO);
    public int deleteChapterNode(ChapterNoteDeleteDTO chapterNoteDeleteDTO);
//    public int updateChapterLeafNode(ChapterLeafNodeDTO chapterLeafNodeDTO);
    public int updateChapterNode(ChapterNodeDTO chapterNodeDTO);
}
