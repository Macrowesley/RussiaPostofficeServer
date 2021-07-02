package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 【待定】 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:27
 */
@Deprecated
@Data
@TableName("rcs_registers")
public class Registers {

    /**
     * 金额（单位是分）【待定：什么金额】
     */
    @TableField("amount")
    private String amount;

    /**
     * 【待定】
     */
    @TableField("asc_register")
    private String ascRegister;

    /**
     * 合同id
     */
    @TableField("contract_code")
    private String contractCode;

    /**
     * 合同号
     */
    @TableField("contract_num")
    private Integer contractNum;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 【待定】
     */
    @TableField("dec_register")
    private String decRegister;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;

    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 【待定】
     */
    @TableField("postofficeIndex")
    private String postofficeindex;

    /**
     * 【待定】1 REFILL  2 REFUND
     */
    @TableField("r_type")
    private Integer rType;

}
