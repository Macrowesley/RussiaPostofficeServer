package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 邮政产品表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:46:18
 */
@Data
@TableName("rcs_postal_product")
public class PostalProduct {

    /**
     * 【待定】
     */
    @TableField("code")
    private String code;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 运输方向  1-内部（RTM-2）;  2-外向国际（RTM-2）;
     */
    @TableField("directctg")
    private Integer directctg;

    /**
     * 偏远地区； 1-长达2000公里，2-超过2000公里
     */
    @TableField("distance_type")
    private Integer distanceType;

    /**
     * 【待定】
     */
    @TableId(value = "id")
    private String id;

    /**
     * 邮件类别的名称
									 #*简单-0
									 #*定制-1
									 #*申报价值-2
     */
    @TableField("mail_ctg")
    private Integer mailCtg;

    /**
     * 邮件类型代码  2-字母  3-包裹邮寄
     */
    @TableField("mail_type")
    private Integer mailType;

    /**
     * 邮政物品的最大重量
     */
    @TableField("max_weight")
    private Integer maxWeight;

    /**
     * 【待定】
     */
    @TableField("name")
    private String name;

    /**
     * 转发区域； 1-内部，2-外部
     */
    @TableField("region_type")
    private Integer regionType;

    /**
     * 
     */
    @TableField("tax_id")
    private Integer taxId;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 关税区（值1-5）
     */
    @TableField("zone_code")
    private Integer zoneCode;

}
