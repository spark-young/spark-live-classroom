package top.spark.live.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.spark.live.constant.ScheduleConstant;
import top.spark.live.dto.LessonInfoDTO;
import top.spark.live.dto.ScheduleDayDTO;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.result.ResultCode;
import top.spark.live.service.ScheduleService;

@RestController
@RequestMapping("Schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/Calendar/All/Teacher")
    public JsonResult getAllCalendarTeacher(@RequestParam("tid") long tid) {
        return JsonResultFactory.getInstance().buildSuccessResult(scheduleService.getCalendarAllTeacher(tid));
    }

    @GetMapping("/Calendar/All/Student")
    public JsonResult getAllCalendarStudent(@RequestParam("sid") long sid) {
        return JsonResultFactory.getInstance().buildSuccessResult(scheduleService.getCalendarAllStudent(sid));
    }

    @PostMapping("/Schedule/Day/Teacher")
    public JsonResult getScheduleDayTeacher(@RequestBody ScheduleDayDTO scheduleDayDTO) {
        System.out.println(JSON.toJSONString(scheduleDayDTO));
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(scheduleService.getScheduleDetailTeacher(scheduleDayDTO)));
    }

    @PostMapping("/Schedule/Day/Student")
    public JsonResult getScheduleDayStudent(@RequestBody ScheduleDayDTO scheduleDayDTO) {
        System.out.println(JSON.toJSONString(scheduleDayDTO));
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(scheduleService.getScheduleDetailStudent(scheduleDayDTO)));
    }

    @GetMapping("Schedule/Today/ClassCount")
    public JsonResult getTodayClassCount(@RequestParam("id") long id) {
        return JsonResultFactory.getInstance().buildSuccessResult(scheduleService.getTodayClassCount(id));
    }

    @PostMapping("Schedule/Lesson/Set")
    public JsonResult setLesson(@RequestBody LessonInfoDTO lessonInfoDTO) {
        JsonResult success = JsonResultFactory.getInstance().buildSuccessResult();
        JsonResult fail = JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
        switch (scheduleService.setLessonInfo(lessonInfoDTO)) {
            case ScheduleConstant.ADD_LESSON_SUCCESS:
                success.setMessage("添加开课时间成功");
                return success;
            case ScheduleConstant.MODIFY_LESSON_SUCCESS:
                success.setMessage("变更课程时间请求提交成功！");
                return success;
            case ScheduleConstant.ADD_OR_MODIFY_ERROR:
                return fail;
            case ScheduleConstant.REQUEST_HAS_SEND:
                fail.setMessage("您已提交该课程的修改时间请求，请等待处理结果！");
                return fail;
            case ScheduleConstant.LESSON_TIME_REPETITION:
                fail.setMessage("您设定的时间与其他开课时间冲突，请重新选择！");
                return fail;
        }
        return fail;
    }
}
