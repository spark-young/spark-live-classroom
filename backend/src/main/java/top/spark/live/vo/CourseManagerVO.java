package top.spark.live.vo;

import lombok.Data;

@Data
public class CourseManagerVO {
    private long id;
    private String cname;
    private String tname;
    private String typeName;
    private String description;
    private boolean status;
}
