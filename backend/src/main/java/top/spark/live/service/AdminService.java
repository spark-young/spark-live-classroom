package top.spark.live.service;

import top.spark.live.vo.CourseManagerVO;
import top.spark.live.vo.TeacherInfoVO;
import top.spark.live.vo.UserManagerVO;

import java.util.List;

public interface AdminService {
    public List<UserManagerVO> teacherList();
    public List<UserManagerVO> studentList();

    public List<CourseManagerVO> categoryToCourseList(long typeId);
    public List<CourseManagerVO> teacherToCourseList(long tId);

    public boolean changeUserStatus(long id);
    public boolean changeCourseStatus(long id);

}
