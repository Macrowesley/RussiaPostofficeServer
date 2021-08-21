package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 税率表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
@Data
@TableName("rcs_tax")
public class Tax {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 描述
     */
    @TableField("description")
    private String description;


    /**
     * 版本信息
     */
    @TableField("version")
    private String version;

    @TableField("source")
    private String source;

    @TableField("save_path")
    private String savePath;

    /**
     * 【待定】
     */
    @TableField("apply_date")
    private Date applyDate;

    /**
     * 【待定】
     */
    @TableField("modified")
    private Date modified;

    /**
     * 创建时间
     */
    @TableField("created_date")
    private Date createdDate;

    /**
     * 【待定】
     */
    @TableField("publish_date")
    private Date publishDate;

    /**
     * 是否通知俄罗斯 0 未通知 1 已通知
     */
    @TableField("inform_russia")
    private Integer informRussia;

}
