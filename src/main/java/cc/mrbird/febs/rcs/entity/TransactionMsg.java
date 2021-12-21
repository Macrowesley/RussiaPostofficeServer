package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("rcs_transaction_msg")
public class TransactionMsg {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * format: uuid
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 产品code
     */
    @TableField("code")
    private String code;

    @TableField("count")
    private Long count;
    //单位是分，应该改成long类型
    @TableField("amount")
    private Long amount;

    @TableField("dm_msg")
    private String dmMsg;

    @TableField("frank_machine_id")
    private String frankMachineId;

    //1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2
    @TableField("status")
    private String status;

    @TableField("created_time")
    private Date createdTime;
}
