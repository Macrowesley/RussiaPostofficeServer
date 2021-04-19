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
 * @date 2021-04-17 14:45:39
 */
@Data
@TableName("rcs_statistics")
public class Statistics {

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
     * 【待定】
     */
    @TableField("final_piece_counter")
    private Long finalPieceCounter;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;

    /**
     * 【待定】
     */
    @TableField("franking_period_end")
    private Date frankingPeriodEnd;

    /**
     * 【待定】
     */
    @TableField("franking_period_start")
    private Date frankingPeriodStart;

    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 【待定】
     */
    @TableField("initial_piece_counter")
    private Long initialPieceCounter;

    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private String totalAmount;

}
