package top.spark.live.vo;

import lombok.Data;

@Data
public class CategoryListVO {
    private long id;
    private String title;
    private String description;
    public CategoryListVO(long id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
