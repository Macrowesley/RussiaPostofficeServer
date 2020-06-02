package cc.mrbird.febs.order.entity;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
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
@Excel
@TableName("t_order")
public class Order {

    /**
     * 订单id
     */
    @ExcelField("订单id")
    @TableId(value = "order_id", type = IdType.AUTO)
    Long orderId;

    /**
     * 设备id
     */
    @TableField("device_id")
    Long deviceId;

    /**
     * 设备id
     */
    @ExcelField("表头号")
    @TableField("acnum")
    String acnum;

    /**
     * 订单号
     */
    @ExcelField("订单号")
    @TableField("order_number")
    String orderNumber;

    /**
     * 订单金额（单位为分）
     */
    @ExcelField("订单金额（单位为分）")
    @TableField("amount")
    String amount;

    /**
     *  订单进度
        0未开始
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
    @ExcelField("订单进度 \t\n" +
            "        0未开始\n" +
            "        1.发起申请\n" +
            "        2.审核中\n" +
            "        3.审核通过   4 驳回\n" +
            "        5.机器获取数据包\n" +
            "        6.机器注资成功\n" +
            "        7.撤销\n" +
            "        8.冻结\n" +
            "        9.闭环申请中\n" +
            "        10.闭环申请审核失败")
    @TableField("order_status")
    String orderStatus;

    /**
     * 冻结前的状态
     */
    @TableField("old_status")
    private String oldStatus;

    /**
     * 申请人id
     */
    @TableField("apply_user_id")
    Long applyUserId;

    /**
     * 审核人id
     */
    @TableField("audit_user_id")
    Long auditUserId;

    /**
     * 闭环人id
     */
    @TableField("close_user_id")
    Long closeUserId;

    /**
     * 过期天数
     */
    @ExcelField("过期天数")
    @TableField("expire_days")
    int expireDays;

    /**
     * 截止日期
     */
    @ExcelField("截止日期")
    @TableField("end_time")
    Date endTime;

    /**
     * 添加时间
     */
    @ExcelField("添加时间")
    @TableField("create_time")
    Date createTime;

}
