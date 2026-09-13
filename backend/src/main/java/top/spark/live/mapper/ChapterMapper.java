package top.spark.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.spark.live.entity.Chapter;
import top.spark.live.vo.ChapterVO;

import java.util.List;

public interface ChapterMapper extends BaseMapper<Chapter> {
    @Select("select * from tc_chapter where cid = ${courseId} order by sort")
    public List<Chapter> getChapterByCourseId(@Param("courseId") long courseId);
    @Select("select ch.*,sc.class_time from tc_chapter ch left join tc_schedule sc on ch.id = sc.chid where ch.cid = ${cid}")
    public List<ChapterVO> getChapterAndClassTimeByCid(@Param("cid") long cid);
//    @Delete("<script> delete from tc_chapter where id in <foreach collection='array' item='id' open='('" +
//            " separator=',' close=')'>#{id}</foreach></script>")
//    public long[] deleteChapterNode(@Param("ids") long[] ids);
}
