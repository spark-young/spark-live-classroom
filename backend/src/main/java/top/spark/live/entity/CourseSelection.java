package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tc_course_selection")
@Data
public class CourseSelection {
    @TableField("cid")
    private long cid;

    @TableField("tid")
    private long tid;

    @TableField("sid")
    private long sid;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
