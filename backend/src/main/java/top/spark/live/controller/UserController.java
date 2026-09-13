package top.spark.live.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.spark.live.dto.NoteDTO;
import top.spark.live.dto.NoteModifyDTO;
import top.spark.live.dto.UserInfoDTO;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.result.ResultCode;
import top.spark.live.service.UserService;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/Teacher/Info")
    public JsonResult getTeacherInfo(@RequestParam("tid") long tid){
        return JsonResultFactory.getInstance().buildSuccessResult(userService.getTeacherInfo(tid));
    }
    @GetMapping("/Student/Info")
    public JsonResult getStudentInfo(@RequestParam("sid") long sid){
        return JsonResultFactory.getInstance().buildSuccessResult(userService.getStudentInfo(sid));
    }
    @PostMapping("/User/ModifyInfo")
    public JsonResult modifyUserInfo(@RequestBody UserInfoDTO userInfoDTO){
        return userService.modifyUserInfo(userInfoDTO)?JsonResultFactory.getInstance().buildSuccessResult() :
                JsonResultFactory.getInstance().buildFailResult(ResultCode.DATABASE_UPDATE_FAIL);
    }

    @PostMapping("User/AddNote")
    public JsonResult addNote(@RequestBody NoteDTO noteDTO){
        return userService.addNewNote(noteDTO) ? JsonResultFactory.getInstance().buildSuccessResult() : JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
    }

    @PostMapping("User/ModifyNote")
    public JsonResult modifyNote(@RequestBody NoteModifyDTO noteModifyDTO){
        return userService.modifyNote(noteModifyDTO) ? JsonResultFactory.getInstance().buildSuccessResult() : JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
    }

    @GetMapping("User/DeleteNote")
    public JsonResult deleteNote(@RequestParam("id") long id){
        return userService.deleteNote(id) ? JsonResultFactory.getInstance().buildSuccessResult() : JsonResultFactory.getInstance().buildFailResult(ResultCode.COMMON_FAIL);
    }

    @GetMapping("User/ListNode")
    public JsonResult getNoteList(@RequestParam("sid") long sid){
        return JsonResultFactory.getInstance().buildSuccessResult(JSON.toJSONString(userService.getNoteList(sid)));
    }
}
