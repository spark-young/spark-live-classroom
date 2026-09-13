package top.spark.live.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.spark.live.constant.CourseConstant;
import top.spark.live.dto.*;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.result.ResultCode;
import top.spark.live.service.CategoryService;
import top.spark.live.service.CourseService;

@RestController
@RequestMapping("Course")
public class CourseController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;

    @GetMapping("Category/List")
    public JsonResult getCategoryList() {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(categoryService.getCategoryList()));
    }

    @GetMapping("Course/List")
    public JsonResult getCourseList() {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseList()));
    }

    @GetMapping("Course/ListByType")
    public JsonResult getCourseListByType(@RequestParam("typeId") long typeId) {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseListByTypeId(typeId)));
    }

    @GetMapping("Course/ListByTid")
    public JsonResult getCourseListByTid(@RequestParam("tid") long tid) {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseListByTid(tid)));
    }

    @GetMapping("Course/ListBySid")
    public JsonResult getCourseListBySid(@RequestParam("sid") long sid) {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseListBySid(sid)));
    }

    @GetMapping("Course/List/Search")
    public JsonResult getCourseListBySearch(@RequestParam("content") String content){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseListBySearch(content)));
    }

    @GetMapping("Course/Details")
    public JsonResult getCourseDetails(@RequestParam("courseId") long courseId) {
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(courseService.getCourseDetails(courseId)));
    }

    @GetMapping("Course/Subscribe")
    public JsonResult subScribeCourse(@RequestParam("courseId") long courseId, @RequestParam("sid") long sid) {
        JsonResult success = JsonResultFactory.getInstance().buildSuccessResult();
        JsonResult fail = JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
        switch (courseService.subcribeCourse(courseId, sid)) {
            case CourseConstant.STUDENT_HAS_SUBCRIBE:
                fail.setMessage("您已订阅该课程！");
                return fail;
            case CourseConstant.CLASS_HAS_MAX_COUNT:
                fail.setMessage("该课程已达到最大订阅数量！");
                return fail;
            case CourseConstant.SUBCRIBE_SUCCESS:
                success.setMessage("订阅课程成功！");
                return success;
        }
        return fail;
    }

    @PostMapping("Course/ModifyInfo")
    public JsonResult ModifyCourseInfo(@RequestBody CourseInfoDTO courseInfoDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.modifyCourseInfo(courseInfoDTO));
    }

    @PostMapping("Course/Add")
    public JsonResult AddCourse(@RequestBody CourseAddDTO courseAddDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.addCourseInfo(courseAddDTO));
    }

    @GetMapping("Course/Delete")
    public JsonResult DeleteCourse(@RequestParam("cid") long cid) {
        switch (courseService.deleteCourse(cid)) {
            case CourseConstant.DELETE_COURSE_SUCCESS:
                return JsonResultFactory.getInstance().buildSuccessResult();
            case CourseConstant.DELETE_FAIL_COURSE_SELECTED:
                return JsonResultFactory.getInstance().buildFailResult(ResultCode.COURSE_ALREADY_SELECTED);
            case CourseConstant.DELETE_FAIL_UNKNOW:
                return JsonResultFactory.getInstance().buildFailResult(ResultCode.DATABASE_DELETE_FAIL);
        }
        return JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
    }

    @PostMapping("Course/Unsubcribe")
    public JsonResult UnsubscribeCourse(@RequestBody UnsubcribeCourseDTO unsubcribeCourseDTO) {
        return courseService.UnsubcribeCourse(unsubcribeCourseDTO) ? JsonResultFactory.getInstance().buildSuccessResult() : JsonResultFactory.getInstance().buildFailResult(ResultCode.REQUEST_HAS_EXISTED);
    }

    @GetMapping("Chapter/LessonInfo")
    public JsonResult getLessonInfo(@RequestParam("chid") long chid) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.getLessonInfo(chid));
    }

    @GetMapping("Chapter/Select")
    public JsonResult getChapterList(@RequestParam("cid") long cid) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.getChapter(cid));
    }

    @PostMapping("/Chapter/Insert/Root")
    public JsonResult addChapterRoot(@RequestBody ChapterRootDTO chapterRootDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.insertChapterRoot(chapterRootDTO));
    }

    @PostMapping("/Chapter/Insert/Node")
    public JsonResult addChapterNode(@RequestBody ChapterNodeDTO chapterNodeDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.insertChapterNode(chapterNodeDTO));
    }

    @PostMapping("/Chapter/Delete")
    public JsonResult deleteChapterNode(@RequestBody ChapterNoteDeleteDTO chapterNoteDeleteDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.deleteChapterNode(chapterNoteDeleteDTO));
    }

    @PostMapping("/Chapter/Update/Node")
    public JsonResult updateChapterNode(@RequestBody ChapterNodeDTO chapterNodeDTO) {
        return JsonResultFactory.getInstance().buildSuccessResult(courseService.updateChapterNode(chapterNodeDTO));
    }
//    @PostMapping("/Chapter/Update/Leaf")
//    public JsonResult updateChapterLeafNode(@RequestBody ChapterLeafNodeDTO chapterLeafNodeDTO){
//        return JsonResultFactory.getInstance().buildSuccessResult(courseService.updateChapterLeafNode(chapterLeafNodeDTO));
//    }
}
