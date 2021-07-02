package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 交易数据表【待定】 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:46:25
 */
@Deprecated
@Data
@TableName("rcs_transaction_data")
public class TransactionData {

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 【待定】
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 
     */
    @TableField("statistics_id")
    private String statisticsId;

    /**
     * 【待定】
     */
    @TableField("t_amount")
    private String tAmount;

    /**
     * 数量
     */
    @TableField("t_count")
    private Integer tCount;

    /**
     * 重量
     */
    @TableField("t_weight")
    private Integer tWeight;

    /**
     * tax版本
     */
    @TableField("tax_version")
    private String taxVersion;

}
