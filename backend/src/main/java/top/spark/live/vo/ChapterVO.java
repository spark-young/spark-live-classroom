package top.spark.live.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterVO {
    private long id;
    private String title;
    private String description;
    private long nid;
    private long crid;
    private String section;
    private List<ChapterVO> children;
    public ChapterVO(){
        this.children = new ArrayList<>();
    }
    public ChapterVO(long id,String title,String description,long nid,long crid,String section){
        this.id = id;
        this.title = title;
        this.description = description;
        this.nid = nid;
        this.crid = crid;
        this.section = section;
        this.children = new ArrayList<>();
    }
}
