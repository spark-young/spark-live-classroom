package top.spark.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import top.spark.live.constant.Direction;
import top.spark.live.constant.RequestStatus;
import top.spark.live.constant.RequestType;
import top.spark.live.constant.RoleConstant;
import top.spark.live.entity.*;
import top.spark.live.mapper.*;
import top.spark.live.service.AdminService;
import top.spark.live.vo.CourseManagerVO;
import top.spark.live.vo.UserManagerVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Override
    public List<UserManagerVO> teacherList() {
        QueryWrapper<UserRoleRelation> qwur = new QueryWrapper<>();
        qwur.lambda().eq(UserRoleRelation::getRoleId, RoleConstant.TEACHER);

        List<UserRoleRelation> list = userRoleRelationMapper.selectList(qwur);
        List<Long> ids = new ArrayList<>();

        list.forEach((e) -> ids.add(e.getUserId()));

        List<User> users = userMapper.selectBatchIds(ids);

        List<UserManagerVO> result = new ArrayList<>();
        users.forEach((e) -> {
            UserManagerVO userManagerVO = new UserManagerVO();
            userManagerVO.setId(e.getId());
            userManagerVO.setName(e.getUserId());
            userManagerVO.setEmail(e.getEmail());
            userManagerVO.setMobile(e.getMobile());
            userManagerVO.setStatus(e.getUserStatus().equals("1"));
            result.add(userManagerVO);
        });
        return result;
    }

    @Override
    public List<UserManagerVO> studentList() {
        QueryWrapper<UserRoleRelation> qwur = new QueryWrapper<>();
        qwur.lambda().eq(UserRoleRelation::getRoleId, RoleConstant.STUDENT);

        List<UserRoleRelation> list = userRoleRelationMapper.selectList(qwur);
        List<Long> ids = new ArrayList<>();

        list.forEach((e) -> ids.add(e.getUserId()));

        List<User> users = userMapper.selectBatchIds(ids);

        List<UserManagerVO> result = new ArrayList<>();
        users.forEach((e) -> {
            UserManagerVO userManagerVO = new UserManagerVO();
            userManagerVO.setId(e.getId());
            userManagerVO.setName(e.getUserId());
            userManagerVO.setEmail(e.getEmail());
            userManagerVO.setMobile(e.getMobile());
            userManagerVO.setStatus(e.getUserStatus().equals("1"));
            result.add(userManagerVO);
        });
        return result;
    }

    @Override
    public List<CourseManagerVO> categoryToCourseList(long typeId) {
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.lambda().eq(Course::getTypeId,typeId);

        List<Course> list = courseMapper.selectList(qw);
        List<CourseManagerVO> result = new ArrayList<>();

        list.forEach((e) -> {
            CourseManagerVO courseManagerVO = new CourseManagerVO();
            courseManagerVO.setId(e.getId());
            courseManagerVO.setTname(userMapper.selectById(e.getTid()).getUserId());
            courseManagerVO.setCname(e.getName());
            courseManagerVO.setDescription(e.getDescription());
            courseManagerVO.setStatus(e.getStatus().equals("1"));
            result.add(courseManagerVO);
        });
        return result;
    }

    @Override
    public List<CourseManagerVO> teacherToCourseList(long tId) {
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.lambda().eq(Course::getTid,tId);

        List<Course> list = courseMapper.selectList(qw);
        List<CourseManagerVO> result = new ArrayList<>();

        list.forEach((e) -> {
            CourseManagerVO courseManagerVO = new CourseManagerVO();
            courseManagerVO.setId(e.getId());
            courseManagerVO.setTypeName(categoryMapper.selectById(e.getTypeId()).getTitle());
            courseManagerVO.setCname(e.getName());
            courseManagerVO.setDescription(e.getDescription());
            courseManagerVO.setStatus(e.getStatus().equals("1"));
            result.add(courseManagerVO);
        });
        return result;
    }

    @Override
    public boolean changeUserStatus(long id) {
        User user = userMapper.selectById(id);
        user.setUserStatus(user.getUserStatus().equals("1")?"2":"1");
        return userMapper.updateById(user) == 1;
    }

    @Override
    public boolean changeCourseStatus(long id) {
        Course course = courseMapper.selectById(id);
        course.setStatus(course.getStatus().equals("1")?"2":"1");

        QueryWrapper<CourseSelection> qw = new QueryWrapper<>();
        qw.lambda().eq(CourseSelection::getCid,id);

        Request request1 = new Request();
        request1.setTid(course.getTid());
        request1.setSid(1);
        request1.setStatus(RequestStatus.NOT_SOLVE);
        request1.setParamJson("");
        request1.setRid(RequestType.NOTIFICATION);
        request1.setContent("管理员"+(course.getStatus().equals("1")?"上线了":"下线了")+course.getName()+(course.getStatus().equals("1")?"":"；若有疑问，请联系admin@spark.com"));
        request1.setDirection(Direction.ADMIN_TO_ALL);
        requestMapper.insert(request1);

        List<CourseSelection> list = courseSelectionMapper.selectList(qw);
        list.forEach((e) -> {
            Request request =new Request();
            request.setTid(1);
            request.setSid(e.getSid());
            request.setStatus(RequestStatus.NOT_SOLVE);
            request.setParamJson("");
            request.setRid(RequestType.NOTIFICATION);
            request.setContent("管理员"+(course.getStatus().equals("1")?"上线了":"下线了")+course.getName());
            request.setDirection(Direction.ADMIN_TO_ALL);
            requestMapper.insert(request);
        });
        return courseMapper.updateById(course) == 1;
    }
}
