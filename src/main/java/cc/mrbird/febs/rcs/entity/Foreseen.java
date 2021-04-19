package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 预算订单 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:44:51
 */
@Data
@TableName("rcs_foreseen")
public class Foreseen {

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

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
     * 0 流程中 1 闭环
     */
    @TableField("foreseen_status")
    private Integer foreseenStatus;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;

    /**
     * 主键uuid
     */
    @TableId(value = "id")
    private String id;

    /**
     * 【待定】
     */
    @TableField("mail_val")
    private String mailVal;

    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;

    /**
     * tax版本
     */
    @TableField("tax_version")
    private String taxVersion;

    /**
     * 总数量
     */
    @TableField("total_count")
    private Integer totalCount;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
