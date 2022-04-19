package cc.mrbird.febs.rcs.entity;


import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
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
@Excel("printJob Excel")
public class PrintJob {

    /**
     * 自增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("contract_code")
    @ExcelField(value = "contractCode")
    private String contractCode;

    @TableField("foreseen_id")
    @ExcelField(value = "foreseenId")
    private String foreseenId;

    @TableField("transaction_id")
    @ExcelField(value = "transactionId")
    private String transactionId;
    
    @TableField("user_id")
    @ExcelField(value = "userId")
    String userId;

    @TableField("pc_user_id")
    @ExcelField(value = "pcUserId")
    Integer pcUserId;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    @ExcelField(value = "contractCode")
    private String frankMachineId;


    /**
     * 整个流程状态
     */
    @TableField("flow")
    private Integer flow;

    @TableField("flow_detail")
    private Integer flowDetail;

    @TableField("total_count")
    @ExcelField(value = "totalCount")
    int totalCount;

    @TableField("total_amount")
    @ExcelField(value = "totalAmount")
    Double totalAmount;
    /**
     * 1 机器创建的订单 2 管理页面创建的订单
     */
    @TableField("type")
    @ExcelField(value = "type", writeConverterExp = "1=机器订单,2=网页订单")
    private Integer type;


    /**
     * 取消原因
     */
    @TableField("cancel_msg_code")
    private Integer cancelMsgCode;

    @TableField("updated_time")
    @ExcelField(value = "updatedTime", writeConverter = TimeConverter.class)
    private Date updatedTime;

    @TableField("created_time")
    @ExcelField(value = "createdTime", writeConverter = TimeConverter.class)
    private Date createdTime;

}
