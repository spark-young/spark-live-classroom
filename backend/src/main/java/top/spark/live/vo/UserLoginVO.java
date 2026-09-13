package top.spark.live.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private long id;
    private String userId;
    private long roleId;
    public UserLoginVO(long id,String userId,long roleId){
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }
}
