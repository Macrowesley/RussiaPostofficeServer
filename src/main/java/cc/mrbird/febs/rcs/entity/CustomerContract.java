package cc.mrbird.febs.rcs.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户-合同关系表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:46:14
 */
@Data
@TableName("rcs_customer_contract")
public class CustomerContract {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private String customerId;


}
