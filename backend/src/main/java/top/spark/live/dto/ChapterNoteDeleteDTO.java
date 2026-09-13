package top.spark.live.dto;

import lombok.Data;

@Data
public class ChapterNoteDeleteDTO {
    //需要删除的所有chid，首个为左根节点
    private Long[] ids;
    //当前左根节点的上一邻节点
    private long preChid;
    //当前左根节点的父节点
    private long parChid;
}
