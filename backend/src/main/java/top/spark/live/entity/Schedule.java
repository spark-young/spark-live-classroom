package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tc_schedule")
@Data
public class Schedule {
    @TableId(value = "cid",type = IdType.NONE)
    private long cid;

    @TableId(value = "chid",type = IdType.NONE)
    private long chid;

    @TableId(value = "sid",type = IdType.NONE)
    private long sid;

    @TableField("tid")
    private long tid;

    @TableField("room_id")
    private String roomId;

    @TableField("class_time")
    private LocalDateTime classTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
