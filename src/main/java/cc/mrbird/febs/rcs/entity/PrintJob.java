package cc.mrbird.febs.rcs.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 打印任务表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:44:46
 */
@Data
@TableName("rcs_print_job")
public class PrintJob {

    /**
     * 取消原因
     */
    @TableField("cancel_msg")
    private String cancelMsg;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * foreseen id
     */
    @TableField("foreseen_id")
    private String foreseenId;

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
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 整个流程状态 0 未闭环  1 正常闭环 上面2个都为1才真正的闭环  -1 点击了取消job
     */
    @TableField("job_flow")
    private Integer jobFlow;

    /**
     * 
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 0 流程中 1 闭环
     */
    @TableField("transaction_status")
    private Integer transactionStatus;

}
