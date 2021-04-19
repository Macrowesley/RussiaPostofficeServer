package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 【待定】这个表的父表是哪个？Statistics还是Transaction Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:32
 */
@Data
@TableName("rcs_frank")
public class Frank {

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 【待定】
     */
    @TableField("dm_message")
    private String dmMessage;

    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 【待定 2个id都需要还是只需要一个？】
     */
    @TableField("statistics_id")
    private String statisticsId;

    /**
     * 【待定 2个id都需要还是只需要一个？】
     */
    @TableField("transaction_id")
    private String transactionId;

}
