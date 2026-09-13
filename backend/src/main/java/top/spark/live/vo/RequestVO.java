package top.spark.live.vo;

import lombok.Data;

@Data
public class RequestVO {
    private long id;
    private String requestName;
    private String requestType;
    private String requestContent;
    private String requestReason;
}
