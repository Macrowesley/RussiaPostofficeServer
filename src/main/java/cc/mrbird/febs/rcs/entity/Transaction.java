package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 交易表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
@Data
@TableName("rcs_transaction")
public class Transaction {

    /**
     * format: uuid
     */
    @TableId(value = "id")
    private String id;

    /**
     * foreseen id
     */
    @TableField("foreseen_id")
    private String foreseenId;


    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;


    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;

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
     * 开始时间
     */
    @TableField("start_time")
    private String startTime;

    /**
     * 停止时间
     */
    @TableField("stop_time")
    private String stopTime;

    @TableField("user_id")
    private String userId;

    /**
     * 【待定】
     */
    @TableField("credit_val")
    private Double creditVal;

    /**
     * 金额（单位是分）
     */
    @TableField("mail_val")
    private Double mailVal;

    /**
     * 总数量
     */
    @TableField("total_count")
    private Integer count;

    /**
     * 【待定】
     */
    @TableField("graph_id")
    private String graphId;

    /**
     * tax版本
     */
    @TableField("tax_version")
    private String taxVersion;

    /**
     * 0 流程中 1 闭环
     */
    @TableField("transaction_status")
    private Integer transactionStatus;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
