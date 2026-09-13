package top.spark.live.dto;

import lombok.Data;

@Data
public class UnsubcribeCourseDTO {
    private long cid;
    private long sid;
    private String content;
}
