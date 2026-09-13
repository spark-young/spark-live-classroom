package top.spark.live.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@TableName("tc_category")
@Data
public class Category {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("pid")
    private long pid;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

}
