package top.spark.live.dto;

import lombok.Data;

@Data
public class CourseAddDTO {
    private long tid;
    private long typeId;
    private String name;
    private String description;
    private int subCount;
    private String url;
}
