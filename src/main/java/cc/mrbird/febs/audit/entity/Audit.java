package cc.mrbird.febs.audit.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 审核表 Entity
 */
@Data
@TableName("t_audit")
public class Audit {

    /**
     * 审核id
     */
    @TableId(value = "audit_id", type = IdType.AUTO)
    private Long auditId;

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
     * 审核类型 1 请求注资  2 请求闭环 
     */
    @TableField("audit_type")
    private String auditType;

    /**
     * 表头号
     */
    @TableField("acnum")
    private String acnum;

    /**
     * 提交人id
     */
    @TableField("f_user_id")
    private Long fUserId;

    /**
     * 提交原因
     */
    @TableField("submit_info")
    private String submitInfo;

    /**
     * 审核状态 0 未开始 1 成功 2 驳回  3 冻结中  4 已注销
     */
    @TableField("status")
    private String status;

    /**
     * 冻结前的状态
     */
    @TableField("old_status")
    private String oldStatus;

    /**
     * 审核备注
     */
    @TableField("check_remark")
    private String checkRemark;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

}
