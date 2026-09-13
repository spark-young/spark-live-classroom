package top.spark.live.service;

import top.spark.live.dto.UserSignUpDTO;
import top.spark.live.entity.User;
import top.spark.live.vo.UserLoginVO;

public interface LoginService {
    public UserLoginVO signIn(User user);//返回的是roleId
    public boolean isActive(User user);
    public boolean signUp(UserSignUpDTO userSignUpDTO);
    public boolean checkUserId(String userId);
    public boolean loginAdmin(String pwd);
}
