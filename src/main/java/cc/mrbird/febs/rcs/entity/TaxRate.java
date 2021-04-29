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
     *
     */
    @TableField("postal_product_id")
    private String postalProductId;

    /**
     * 重量，g（上限）
     */
    @TableField("weight")
    private Integer weight;

    /**
     * 不含增值税的关税 RUB
     */
    @TableField("no_vat")
    private Double noVat;


    /**
     * 含增值税关税 RUB
     */
    @TableField("with_vat")
    private Double withVat;

    /**
     * 增值税金额 卢布
     */
    @TableField("sum_vat")
    private Double sumVat;



    /**
     * 增值税率
     */
    @TableField("rate_vat")
    private Double rateVat;






}
