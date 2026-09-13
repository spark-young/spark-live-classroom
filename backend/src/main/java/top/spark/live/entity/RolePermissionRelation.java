package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tu_role_permission_relation")
@Data
public class RolePermissionRelation extends Model<RolePermissionRelation> {
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    private long roleId;
    /**
     * 权限ID
     */
    private long permissionId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
