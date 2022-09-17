package cc.mrbird.febs.rcs.entity;


import cc.mrbird.febs.common.converter.TimeConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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
     * 自增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("contract_code")
    private String contractCode;

    @TableField("foreseen_id")
    private String foreseenId;

    @TableField("transaction_id")
    private String transactionId;
    
    @TableField("user_id")
    String userId;

    @TableField("pc_user_id")
    Integer pcUserId;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;


    /**
     * 整个流程状态
     */
    @TableField("flow")
    private Integer flow;

    @TableField("flow_detail")
    private Integer flowDetail;

    @TableField("total_count")
    int totalCount;

    @TableField("total_amount")
    Double totalAmount;

    /**
     * 创建打印任务时，合同金额
     */
    @TableField("contract_current")
    Double contractCurrent;

    /**
     * 创建打印任务时，合同金额
     */
    @TableField("contract_consolidate")
    Double contractConsolidate;

    /**
     * 1 机器创建的订单 2 管理页面创建的订单
     */
    @TableField("type")
    private Integer type;

    /**
     * 打印对象类型：过戳还是签条
     * 1 stamp
     * 2 stick
     */
    @TableField("print_object_type")
    private Integer printObjectType;


    /**
     * 取消原因
     */
    @TableField("cancel_msg_code")
    private Integer cancelMsgCode;

    @TableField("updated_time")
    private Date updatedTime;

    @TableField("created_time")
    private Date createdTime;

}
