package top.spark.live.dto;

import lombok.Data;

@Data
public class ChapterNodeDTO extends ChapterRootDTO{
    private long id;
    private long parChid;
}
