package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tu_user_role_relation")
@Data
public class UserRoleRelation extends Model<UserRoleRelation> {

    private static final long serialVersionUID = 1L;
    /**
     *用户ID
     */
    private long userId;
    /**
     * 角色ID
     */
    private long roleId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
