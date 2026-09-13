package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tu_permissions")
@Data
public class Permissions {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;

    @TableId(value = "title",type = IdType.NONE)
    private String title;

    @TableField("description")
    private String description;

    @TableField("http_path")
    private String httpPath;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
