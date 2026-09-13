package top.spark.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tc_chapter_root_first")
@Data
public class ChapterRootFirst {
    @TableId(value = "cid", type = IdType.NONE)
    private long cid;
    @TableField("chid")
    private long chid;
}
