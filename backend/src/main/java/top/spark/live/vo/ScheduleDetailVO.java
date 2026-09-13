package top.spark.live.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDetailVO {
    private long sid;
    private long tid;
    private long cid;
    private long chid;
    private String courseName;
    private String typeName;
    private String tName;
    private String sName;
    private String ChapterName;
    private String roomId;
    private LocalDateTime classTime;
}
