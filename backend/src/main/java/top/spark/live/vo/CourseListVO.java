package top.spark.live.vo;

import lombok.Data;

@Data
public class CourseListVO {
    private long id;
    private long typeId;
    private String typeName;
    private int subCount;
    private int curCount;
    private String name;
    private String description;
    private String url;
    public CourseListVO(long id,long typeId,String typeName,String name,String description,String url){
        this.id = id;
        this.typeId = typeId;
        this.typeName = typeName;
        this.name = name;
        this.description = description;
        this.url = url;
    }
    public CourseListVO(long id,long typeId,String typeName,int subCount,String name,String description,String url){
        this.id = id;
        this.typeId = typeId;
        this.typeName = typeName;
        this.subCount = subCount;
        this.name = name;
        this.description = description;
        this.url = url;
    }
}
