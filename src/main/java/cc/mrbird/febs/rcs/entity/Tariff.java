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
@TableName("rcs_tariff")
public class Tariff {

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

    String type;


}
