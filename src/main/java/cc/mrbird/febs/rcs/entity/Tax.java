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
     * 【待定】
     */
    @TableField("apply_time")
    private Date applyTime;

    /**
     * 【待定】
     */
    @TableField("change_time")
    private Date changeTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 流程 0 未闭环   1 闭环(成功后闭环) -1 闭环(失败后闭环)
     */
    @TableField("flow")
    private Integer flow;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮局id
     */
    @TableField("post_office_id")
    private String postOfficeId;

    /**
     * 【待定】
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 【待定：长度，含义，来源】
     */
    @TableField("rcsVersions")
    private String rcsversions;

    /**
     * 版本信息
     */
    @TableField("version")
    private String version;

}
