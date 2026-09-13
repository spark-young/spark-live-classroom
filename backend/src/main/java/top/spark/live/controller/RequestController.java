package top.spark.live.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.spark.live.dto.RequestSolveDTO;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.service.RequestService;

@RestController
@RequestMapping("Request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @GetMapping("/Request/ListByTid")
    public JsonResult getRequestListByTid(@RequestParam("tid") long tid){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(requestService.getTeacherRequest(tid)));
    }
    @GetMapping("/Request/CountByTid")
    public JsonResult getCountByTid(@RequestParam("tid") long tid){
        return JsonResultFactory.getInstance().buildSuccessResult(requestService.getCountTeacher(tid));
    }
    @GetMapping("/Request/ListBySid")
    public JsonResult getRequestListBySid(@RequestParam("sid") long sid){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(requestService.getStudentRequest(sid)));
    }
    @GetMapping("/Request/CountBySid")
    public JsonResult getCountBySid(@RequestParam("sid") long sid){
        return JsonResultFactory.getInstance().buildSuccessResult(requestService.getCountStudent(sid));
    }
    @PostMapping("/Request/Solve")
    public JsonResult solveRequest(@RequestBody RequestSolveDTO requestSolveDTO){
        return JsonResultFactory.getInstance().buildSuccessResult(requestService.solveRequest(requestSolveDTO));
    }
}
