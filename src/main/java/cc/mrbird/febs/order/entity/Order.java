package cc.mrbird.febs.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_order")
public class Order implements Serializable {

    private Long orderId;

    private Long deviceId;

    private String orderNumber;

    private String amount;

    private String acnum;

    private String orderStatus;

    private Long applyUserId;

    private Long auditUserId;

    private Long closeUserId;

    private int expireDays;

    private String isExpire;

    private String isAlarm;

    private Date endTime;

    private Date createTime;
}
