package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tc_chapter")
@Data
public class Chapter {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;

    @TableId(value = "cid",type = IdType.NONE)
    private long cid;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("url")
    private String url;

    @TableField("nid")
    private long nid;

    @TableField("crid")
    private long crid;

    @TableField("status")
    private String status;

    /**
     * 存储章节信息
     * eg:[1,1,3]表示第一章1.3节，
     */
    @TableField("section")
    private String section;

    @TableField("sort")
    private long sort;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
