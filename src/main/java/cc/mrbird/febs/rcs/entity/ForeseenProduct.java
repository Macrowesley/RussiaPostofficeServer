package cc.mrbird.febs.rcs.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    @TableField("print_job_id")
    private Integer printJobId;

    /**
     * 预算订单id
     */
    @TableField("foreseen_id")
    private String foreseenId;


    /**
     * 产品code
     */
    @TableField("code")
    private String productCode;

    /**
     * 数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 批次总重量
     */
    @TableField("weight")
    private Double weight;

    /**
     * 金额
     */
    @TableField("amount")
    private Double amount;

    /**
     * 地址
     */
    @TableField("address")
    String address;



    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
