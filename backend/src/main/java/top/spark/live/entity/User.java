package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tu_user")
@Data
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;

    @TableField("user_id")
    private String userId;

    @TableField("login_pwd")
    private String loginPwd;

    @TableField("email")
    private String email;

    @TableField("mobile")
    private String mobile;

    @TableField("user_status")
    private String userStatus;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
