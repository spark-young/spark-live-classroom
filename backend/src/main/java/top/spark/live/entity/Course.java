package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tc_course")
@Data
public class Course {
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    @TableId(value = "tid", type = IdType.NONE)
    private long tid;

    @TableId(value = "type_id", type = IdType.NONE)
    private long typeId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("url")
    private String url;

    @TableField("sub_count")
    private int subCount;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
