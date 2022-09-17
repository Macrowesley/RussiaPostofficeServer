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
     * 主键uuid
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableId(value = "code")
    String code;

    @TableField(value = "name")
    String name;

    @TableField(value = "mail_type")
    Integer mailType;

    @TableField(value = "mail_ctg")
    Integer mailCtg;

    @TableField(value = "max_weight")
    Integer maxWeight;

    @TableField(value = "trans_type")
    //SURFACE, AIR, ANY
    String transType;

    @TableField(value = "region_type")
    String regionType;

    @TableField(value = "region_zone")
    Integer regionZone;

    //AFTER_2000, UP_2000
    @TableField(value = "distance_type")
    String distanceType;

    @TableField(value = "contract_name")
    String contractName;

    @TableField(value = "numdiff")
    Integer numdiff;

    @TableField(value = "label_ru")
    String labelRu;

    @TableField(value = "is_postal_market_only")
    String isPostalMarketOnly;


    //和PostalProductDTO不同的参数
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

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

    @TableField("modified")
    private Date modified;



}
