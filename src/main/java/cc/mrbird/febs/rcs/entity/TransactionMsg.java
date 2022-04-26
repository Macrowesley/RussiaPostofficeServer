package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("rcs_transaction_msg")
@Document("rcs_transaction_msg")
public class TransactionMsg {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * format: uuid
     */
    @TableField("transaction_id")
    @Field("transaction_id")
    private String transactionId;

    /**
     * 产品code
     */
    @TableField("code")
    @Field("code")
    private String code;

    @TableField("count")
    @Field("count")
    private Long count;

    //单位是分，应该改成long类型
    @TableField("amount")
    @Field("amount")
    private Long amount;


    //单批次预估数量
    @TableField("batchCount")
    @Field("batchCount")
    private Integer batchCount;

    //单个的重量, 单位是g
    @TableField("singleWeight")
    @Field("singleWeight")
    private Integer singleWeight;

    //regionType
    @TableField("regionType")
    @Field("regionType")
    private String regionType;

    //labelRu
    @TableField("labelRu")
    @Field("labelRu")
    private String labelRu;

    //fixedValue
    @TableField("fixedValue")
    @Field("fixedValue")
    private Integer fixedValue;

    @TableField("dm_msg")
    @Field("dm_msg")
    private String dmMsg;

    @TableField("frank_machine_id")
    @Field("frank_machine_id")
    private String frankMachineId;

    //1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2
    @TableField("status")
    @Field("status")
    private String status;

    @TableField("created_time")
    @Field("created_time")
    private Date createdTime;
}
