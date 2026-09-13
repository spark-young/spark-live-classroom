package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tu_request")
@Data
public class Request {
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    @TableField("tid")
    private long tid;

    @TableField("sid")
    private long sid;

    @TableField("rid")
    private long rid;

    @TableField("content")
    private String content;

    @TableField("param_json")
    private String paramJson;

    @TableField("status")
    private int status;
    /**
     * 1 删除课程；2 修改时间
     */
    @TableField("direction")
    private int direction;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
