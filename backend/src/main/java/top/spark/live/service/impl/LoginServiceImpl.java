package top.spark.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.dto.UserSignUpDTO;
import top.spark.live.entity.User;
import top.spark.live.entity.UserRoleRelation;
import top.spark.live.mapper.UserMapper;
import top.spark.live.mapper.UserRoleRelationMapper;
import top.spark.live.service.LoginService;
import top.spark.live.vo.UserLoginVO;

@Service
class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Override
    /**
     * 目下确定一个账户对应一个角色
     */
    public UserLoginVO signIn(User user) {//返回的是roleId
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUserId,user.getUserId()).eq(User::getLoginPwd,user.getLoginPwd());
        User qwUser = userMapper.selectOne(qw);
        if(qwUser!=null){
            QueryWrapper<UserRoleRelation> qw1 = new QueryWrapper<>();
            qw1.lambda().eq(UserRoleRelation::getUserId,qwUser.getId());
            return new UserLoginVO(qwUser.getId(),qwUser.getUserId(),userRoleRelationMapper.selectOne(qw1).getRoleId());
        }
        return null;
    }

    @Override
    public boolean isActive(User user) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUserId,user.getUserId()).eq(User::getLoginPwd,user.getLoginPwd());
        return userMapper.selectOne(qw).getUserStatus().equals("1");
    }

    @Override
    public boolean signUp(UserSignUpDTO userSignUpDTO) {
        User user = new User();
        user.setUserId(userSignUpDTO.getUserId());
        user.setLoginPwd(userSignUpDTO.getLoginPwd());
        if(userMapper.insert(user) == 1){
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.lambda().eq(User::getUserId,userSignUpDTO.getUserId());
            userRoleRelation.setUserId(userMapper.selectOne(qw).getId());
            userRoleRelation.setRoleId(Integer.valueOf(userSignUpDTO.getRole()));
            userRoleRelationMapper.insert(userRoleRelation);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserId(String userId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUserId,userId);
        if(userMapper.selectOne(qw) != null){
            return true;
        }
        return false;
    }
    @Override
    public boolean loginAdmin(String pwd) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUserId,"admin").eq(User::getLoginPwd,pwd);
        return userMapper.selectOne(qw) != null;
    }
}
