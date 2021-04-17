package cc.mrbird.febs.rcs.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 预算订单产品 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:44:55
 */
@Data
@TableName("rcs_foreseen_product")
public class ForeseenProduct {

    /**
     * 金额(单位是分)
     */
    @TableField("amount")
    private String amount;

    /**
     * 预算订单id
     */
    @TableField("foreseen_id")
    private String foreseenId;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数量
     */
    @TableField("p_count")
    private Integer pCount;

    /**
     * 【待定】
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 重量
     */
    @TableField("weight")
    private Integer weight;

}
