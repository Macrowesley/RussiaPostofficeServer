package cc.mrbird.febs.notice.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 消息提示表 Entity
 *
 * @author mrbird
 * @date 2020-06-11 15:30:26
 */
@Data
@TableName("t_notice")
public class Notice {

    /**
     * 消息id
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;


    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 设备id
     */
    @TableField("device_id")
    private Long deviceId;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;


    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;


    /**
     * 订单金额（单位为分）
     */
    @TableField("amount")
    private String amount;


    /**
     * 是否读过 0 未读 1已读
     */
    @TableField("is_read")
    private String isRead;

    @TableField("content")
    private String content;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;




}
