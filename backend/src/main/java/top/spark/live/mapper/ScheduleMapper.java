package top.spark.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.spark.live.entity.Schedule;
import top.spark.live.vo.CalendarVO;
import top.spark.live.vo.LessonInfoVO;

import java.time.LocalDateTime;
import java.util.List;
public interface ScheduleMapper extends BaseMapper<Schedule> {
    @Select("SELECT substring(class_time,1,4) years,substring(class_time,6,2) months,substring(class_time,9,2) days,count(substring(class_time,1,10)) count " +
            "FROM `tc_schedule` where tid = ${tid} and sid != 0 group by substring(class_time,1,4),substring(class_time,6,2),substring(class_time,9,2)")
    public List<CalendarVO> getCalendarAllTeacher(@Param("tid")long tid);

    @Select("SELECT substring(class_time,1,4) years,substring(class_time,6,2) months,substring(class_time,9,2) days,count(substring(class_time,1,10)) count " +
            "FROM `tc_schedule` where sid = ${sid} group by substring(class_time,1,4),substring(class_time,6,2),substring(class_time,9,2)")
    public List<CalendarVO> getCalendarAllStudent(@Param("sid")long sid);

    @Select("select u.id sid,u.user_id s_name,s.class_time from (select id,user_id from tu_user where id in (select sid from tc_course_selection where cid = ${cid})) u left join (select sid,class_time from\n" +
            " tc_schedule where chid = ${chid}) s on u.id = s.sid;")
    public List<LessonInfoVO> getLessonInfo(@Param("cid") long cid,@Param("chid") long chid);

    @Update("update tc_schedule set class_time = #{classTime} where cid = ${cid} and chid = ${chid} and sid = ${sid}")
    public boolean modifySchedule(@Param("classTime")LocalDateTime classTime,@Param("cid") long cid,@Param("chid") long chid,@Param("sid") long sid);

    @Update("update tc_schedule set sid = ${sid} where cid = ${cid}")
    public void subscribeSchedule(@Param("sid") long sid,@Param("cid") long cid);
//    @Select("select sc.*,cs.tid,cs.sid from tc_schedule sc left join tc_course_selection cs on sc.cid = cs.cid where sc.cid in (select id from tc_course where tid = ${tid}) and sc.class_time BETWEEN ${start} and ${end} order by sc.class_time ")
//    public List<Sche>
}
