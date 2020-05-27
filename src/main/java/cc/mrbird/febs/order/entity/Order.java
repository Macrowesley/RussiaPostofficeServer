package cc.mrbird.febs.order.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 订单表 Entity
 *
 *
 * @date 2020-05-27 14:56:29
 */
@Data
@TableName("t_order")
public class Order {

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

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
     * 订单进度 	0未开始
													1.发起申请
													2.审核中
													3.审核通过   4 驳回
													5.机器获取数据包
													6.机器注资成功
													7.撤销
													8.冻结
													9.闭环申请中
													10.闭环申请审核失败
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 申请人id
     */
    @TableField("apply_user_id")
    private Long applyUserId;

    /**
     * 审核人id
     */
    @TableField("audit_id")
    private Long auditId;

    /**
     * 闭环人id
     */
    @TableField("close_cycle_id")
    private Long closeCycleId;

    /**
     * 截止日期
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

}
