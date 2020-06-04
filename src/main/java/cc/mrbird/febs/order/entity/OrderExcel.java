package cc.mrbird.febs.order.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
@Excel("注资报表")
public class OrderExcel {


    /**
     * 订单id
     */
    @ExcelField(value = "订单id")
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
    @ExcelField(value = "表头号")
    @TableField("acnum")
    String acnum;

    /**
     * 订单号
     */
    @ExcelField(value = "订单号")
    @TableField("order_number")
    String orderNumber;

    /**
     * 订单金额
     */
    @ExcelField(value = "订单金额")
    @TableField("amount")
    String amount;

    /**
     * 订单进度
     * 1.创建订单
     * 2.审核中
     * 3.审核通过   4 驳回
     * 5.机器获取数据包
     * 6.机器注资成功
     * 7.撤销
     * 8.冻结
     * 9.闭环申请中
     * 10.闭环申请审核失败
     */
/*    @ExcelField("订单进度 \t\n" +
            "        1.创建订单\n" +
            "        2.审核中\n" +
            "        3.审核通过   4 驳回\n" +
            "        5.机器获取数据包\n" +
            "        6.机器注资成功\n" +
            "        7.机器注资失败\n" +
            "        8.撤销\n" +
            "        9.冻结\n" +
            "        10.闭环申请中\n" +
            "        11.闭环申请审核失败")*/
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
    @ExcelField(value = "过期天数")
    @TableField("expire_days")
    Integer expireDays;

    /**
     * 是否过期 0 未过期 1过期
     */
    @ExcelField(value = "是否过期", writeConverterExp = "0=未过期,1=已过期")
    @TableField("is_expire")
    String isExpire;

    /**
     * 截止日期
     */
    @ExcelField(value = "截止日期", writeConverter = TimeConverter.class)
    @TableField("end_time")
    Date endTime;

    /**
     * 添加时间
     */
    @ExcelField(value = "添加时间", writeConverter = TimeConverter.class)
    @TableField("create_time")
    Date createTime;


    /**
     * 设备昵称
     */
    @ExcelField(value = "设备昵称")
    String nickname;
    /**
     * 审核类型 1 请求注资  2 请求闭环
     */
    String auditType;

    /**
     * 申请人
     */
    @ExcelField(value = "申请人")
    String applyUserName;

    /**
     * 审核人
     */
    @ExcelField(value = "审核人")
    String auditUserName;

    /**
     * 闭环人
     */
//    @ExcelField("闭环人")
    String closeUserName;

    @Override
    public String toString() {
        return "OrderVo{" +
                "acnum='" + acnum + '\'' +
                ", nickname='" + nickname + '\'' +
                ", auditType='" + auditType + '\'' +
                ", applyUserName='" + applyUserName + '\'' +
                ", auditUserName=" + auditUserName +
                ", closeUserName=" + closeUserName +
                ", orderId=" + orderId +
                ", deviceId=" + deviceId +
                ", orderNumber='" + orderNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", applyUserId=" + applyUserId +
                ", auditUserId=" + auditUserId +
                ", closeUserId=" + closeUserId +
                ", expireDays=" + expireDays +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}
