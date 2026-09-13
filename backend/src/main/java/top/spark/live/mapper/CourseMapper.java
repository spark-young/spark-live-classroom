package top.spark.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import top.spark.live.entity.Course;
import top.spark.live.vo.CourseDetailsVO;
import top.spark.live.vo.CourseListVO;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 获取所有course
     * @return
     */
    @Select("select co.id,co.type_id,ca.title,co.name,co.description,co.url from tc_course co left join tc_category ca on co.type_id = ca.id where co.status = 1")
    public List<CourseListVO> getCourseList();

    /**
     * 获取指定type_id的course
     */
    @Select("select co.id,co.type_id,ca.title,co.name,co.description,co.url from tc_course co left join tc_category ca on co.type_id = ca.id where co.status = 1 and co.type_id = ${typeId}")
    public List<CourseListVO> getCourseListByTypeId(@Param("typeId") long typeId);

    /**
     * 获取指定content为课程名的course
     */
    @Select("select co.id,co.type_id,ca.title,co.name,co.description,co.url from tc_course co left join tc_category ca on co.type_id = ca.id where co.status = 1 and co.name like '%${content}%'")
    public List<CourseListVO> getCourseListBySearch(@Param("content") String content);

    /**
     * 获取指定Teacher_id的course
     */
    @Select("select co.id,co.type_id,ca.title,co.sub_count,co.name,co.description,co.url from tc_course co left join tc_category ca on co.type_id = ca.id where co.status = 1 and co.tid = ${tid}")
    public List<CourseListVO> getCourseListByTid(@Param("tid") long tid);

    /**
     * 获取指定Student_id的course
     */
    @Select("select co.id,co.type_id,ca.title,co.name,co.description,co.url from tc_course co left join tc_category ca on co.type_id = ca.id where co.status = 1 and co.id in (select cid from " +
            "tc_course_selection where sid = ${sid})")
    public List<CourseListVO> getCourseListBySid(@Param("sid") long sid);

    /**
     * 获取courseDetails
     * @param courseId
     * @return
     */
    @Select("select co.tid,us.user_id teacher_name,co.name course_name,co.description course_description,co.url course_url from tc_course co inner join " +
            "tu_user us on co.tid = us.id left join tc_course_selection cs" +
            " on co.id = cs.cid where co.id = ${courseId} limit 1")
    public CourseDetailsVO getCourseDetails(@Param("courseId") long courseId);
}
