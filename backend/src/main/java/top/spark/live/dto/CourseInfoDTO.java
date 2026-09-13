package top.spark.live.dto;

import lombok.Data;

@Data
public class CourseInfoDTO {
    private long id;
    private long typeId;
    private String name;
    private String description;
    private int subCount;
}
