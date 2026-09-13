package top.spark.live.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonInfoDTO {
    private long cid;
    private long chid;
    private long tid;
    private long sid;
    private boolean isTeacher;
    private String content;
    private LocalDateTime classTime;
}
