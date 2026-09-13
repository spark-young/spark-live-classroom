package top.spark.live.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonInfoVO {
    private long sid;
    private String sName;
    private LocalDateTime classTime;
}
