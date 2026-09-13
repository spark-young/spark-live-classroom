package top.spark.live.dto;

import lombok.Data;

@Data
public class UserSignUpDTO {
    private String userId;
    private String loginPwd;
    private String role;
}
