package top.spark.live.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.service.AdminService;

@RestController
@RequestMapping("Admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/User/Teacher")
    public JsonResult teacherList(){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(adminService.teacherList()));
    }

    @GetMapping("/User/Student")
    public JsonResult studentList(){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(adminService.studentList()));
    }

    @GetMapping("/Course/Category")
    public JsonResult categoryToCourseList(@RequestParam("id")long id){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(adminService.categoryToCourseList(id)));
    }

    @GetMapping("/Course/Teacher")
    public JsonResult teacherToCourseList(@RequestParam("id")long id){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(adminService.teacherToCourseList(id)));
    }

    @GetMapping("/User/Status")
    public JsonResult userStatusChange(@RequestParam("id") long id){
        return JsonResultFactory.getInstance().buildSuccessResult(adminService.changeUserStatus(id));
    }

    @GetMapping("/Course/Status")
    public JsonResult courseStatusChange(@RequestParam("id") long id){
        return JsonResultFactory.getInstance().buildSuccessResult(adminService.changeCourseStatus(id));
    }

}
