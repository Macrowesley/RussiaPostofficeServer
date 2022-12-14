package cc.mrbird.febs.device.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备表 Entity
 *
 *
 * @date 2020-05-27 14:56:25
 */
@Data
@ToString
@TableName("t_device")
public class Device implements Serializable {

    private static final long serialVersionUID = 5768453074428486556L;
    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    @TableId(value = "device_id", type = IdType.AUTO)
    private Long deviceId;

    /**
     * 表头号
     */
    @ExcelProperty(value = "表头号")
    @NotBlank(message = "{required}")
    @TableField("acnum")
    private String acnum;

    /**
     * 设备昵称
     */
    @ExcelProperty(value = "设备昵称")
    @TableField("nickname")
    private String nickname;

    /**
     * 默认警告金额（分为单位）
     */
    @ExcelProperty(value = "默认警告金额（分为单位）")
    @TableField("warn_amount")
    private String warnAmount;

    /**
     * 最大金额（分为单位）
     */
    @ExcelProperty(value = "最大金额（分为单位）")
    @TableField("max_amount")
    private String maxAmount;

    /**
     * 加密秘钥
     */
    @ExcelProperty(value = "加密秘钥")
    @TableField("secret_key")
    private String secretKey;

    /**
     * 有效天数
     */
    @ExcelProperty(value = "有效天数")
    @TableField("valid_days")
    private Integer validDays;


    /**
     * 是否使用加密锁 0 不使用 1 使用
     */
    @TableField("use_lock")
    private String useLock;

    /**
     * 加密锁信息
     */
    @TableField("lock_info")
    private String lockInfo;

    /**
     * 添加时间
     */
    @TableField("created_time")
    private Date createdTime;

    @TableField(exist=false)
    private int isOnline;

    /**
     * 【待定】机器当前状态
     */
    @TableField("cur_fm_status")
    private Integer curFmStatus;

    /**
     * 机器想要达到的状态
     */
    @TableField("future_fm_status")
    private Integer futureFmStatus;


    @TableField("flow")
    private Integer flow;

    /**
     * 各种FlowXXXEnum的状态
     */
    @TableField("flow_detail")
    private Integer flowDetail;

    /**
     * 谁在改变状态 1 俄罗斯改变状态  0 机器改变状态
     */
    @TableField("is_russia")
    private Integer isRussia;


    /**
     * 错误代码
     */
    @TableField("error_code")
    private String errorCode;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;



    /**
     * 1 STATUS
     2 RATE_TABLE_UPDATE
     */
    @TableField("fm_event")
    private Integer fmEvent;


    /**
     * 机器主键uuid
     */
    @TableField(value = "frank_machine_id")
    private String frankMachineId;

    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;

    /**
     * tax版本 device内部保存的版本 却不一定是最新的版本
     */
    @TableField("tax_version")
    private String taxVersion;

    /**
     * todo 可以考虑加上：是否更新版本，解决版本更新问题
     * tax是否更新 默认为1 最新状态  0 没有更新到最新状态
     */
    @TableField("tax_is_update")
    private Integer taxIsUpdate;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 【待定】
     */
    @TableField("user_id")
    private String userId;
}
