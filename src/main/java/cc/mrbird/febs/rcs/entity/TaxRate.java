package cc.mrbird.febs.rcs.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 税率细节表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:55
 */
@Data
@TableName("rcs_tax_rate")
public class TaxRate {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 不含增值税的关税 RUB（单位是分）
     */
    @TableField("no_vat")
    private String noVat;

    /**
     * 
     */
    @TableField("postal_product_id")
    private String postalProductId;

    /**
     * 增值税率（单位是分）
     */
    @TableField("rate_vat")
    private String rateVat;

    /**
     * 增值税金额 卢布（单位是分）
     */
    @TableField("sum_vat")
    private String sumVat;

    /**
     * 重量，g（上限）
     */
    @TableField("weight")
    private Integer weight;

    /**
     * 含增值税关税 RUB（单位是分）
     */
    @TableField("with_vat")
    private String withVat;

}
