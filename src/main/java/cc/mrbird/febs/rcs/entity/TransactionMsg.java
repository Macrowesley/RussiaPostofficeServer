package cc.mrbird.febs.rcs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("rcs_transaction_msg")
public class TransactionMsg implements Serializable {

    @Id
    private long id;

    /**
     * format: uuid
     */
    @Field("transaction_id")
    private String transactionId;

    /**
     * 产品code
     */
    @Field("code")
    private String code;

    @Field("count")
    private Long count;
    //单位是分，应该改成long类型
    @Field("amount")
    private Long amount;

    @Field("dm_msg")
    private String dmMsg;

    @Field("frank_machine_id")
    private String frankMachineId;

    //1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2
    @Field("status")
    private String status;

    @Field("created_time")
    private Date createdTime;
}
