package top.spark.live.vo;

import lombok.Data;

@Data
public class UserManagerVO {
    private long id;
    private String name;
    private String email;
    private String mobile;
    private boolean status;
}
