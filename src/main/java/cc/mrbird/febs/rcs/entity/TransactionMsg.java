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
    private String foreseenId;

    @TableField("count")
    private Integer count;

    @TableField("amount")
    private Double amount;

    @TableField("dm_msg")
    private String dmMsg;

    @TableField("created_time")
    private Date createdTime;
}
