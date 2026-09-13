package top.spark.live.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteVO {
    private long id;
    private String content;
    private LocalDateTime createTime;
}
