package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 地址 Entity
 *
 * @author mrbird
 */
@Data
@TableName("rcs_contract_address")
public class ContractAddress {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    @TableField("address")
    private String address;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
