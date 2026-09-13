package top.spark.live.dto;

import lombok.Data;

@Data
public class RequestSolveDTO {
    private long id;
    private boolean isAccept;
    private String content;
}
