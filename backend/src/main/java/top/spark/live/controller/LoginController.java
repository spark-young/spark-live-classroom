package top.spark.live.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.spark.live.dto.UserLoginDTO;
import top.spark.live.dto.UserSignUpDTO;
import top.spark.live.entity.User;
import top.spark.live.result.JsonResult;
import top.spark.live.result.JsonResultFactory;
import top.spark.live.result.ResultCode;
import top.spark.live.service.LoginService;
import top.spark.live.vo.UserLoginVO;

@RestController
@RequestMapping("/Login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("SignIn")
    @CrossOrigin
    public JsonResult SignIn(@RequestBody UserLoginDTO userLoginDTO){
        if(!loginService.checkUserId(userLoginDTO.getUserId())){//返回false既是不存在
            return JsonResultFactory.getInstance().buildFailResult(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        User loginUser = new User();
        loginUser.setUserId(userLoginDTO.getUserId());
        loginUser.setLoginPwd(userLoginDTO.getLoginPwd());

        UserLoginVO uvo = loginService.signIn(loginUser);
        if(uvo != null){
            if(!loginService.isActive(loginUser)){
                return JsonResultFactory.getInstance().buildFailResult(ResultCode.USER_IS_UNACTIVED);
            }else{
                return JsonResultFactory.getInstance().buildSuccessResult(uvo);
            }
        }else{
            return JsonResultFactory.getInstance().buildFailResult(ResultCode.USER_CREDENTIALS_ERROR);
        }
    }
    @PostMapping("SignUp")
    @CrossOrigin
    public JsonResult SignUp(@RequestBody UserSignUpDTO userSignUpDTO){
        if(loginService.checkUserId(userSignUpDTO.getUserId())){//返回true既是重复了
            return JsonResultFactory.getInstance().buildFailResult(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        loginService.signUp(userSignUpDTO);
        return JsonResultFactory.getInstance().buildSuccessResult();
    }

    @PostMapping("Admin/Login")
    public JsonResult loginAdmin(@RequestParam("pwd") String pwd){
        return loginService.loginAdmin(pwd) ? JsonResultFactory.getInstance().buildSuccessResult() : JsonResultFactory.getInstance().buildFailResult(ResultCode.USER_CREDENTIALS_ERROR);
    }
}
