package top.spark.live.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChapterRootDTO {
    private long cid;
    private long preChid;
    private String title;
    private String description;
    private String url;
}
