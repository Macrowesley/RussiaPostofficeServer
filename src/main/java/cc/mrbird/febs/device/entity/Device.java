package cc.mrbird.febs.device.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 设备表 Entity
 *
 *
 * @date 2020-05-27 14:56:25
 */
@Data
@TableName("t_device")
public class Device {

    /**
     * 设备id
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Long deviceId;

    /**
     * 表头号
     */
    @TableField("acnum")
    private String acnum;

    /**
     * 设备昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 默认警告金额（分为单位）
     */
    @TableField("warn_amount")
    private String warnAmount;

    /**
     * 最大金额（分为单位）
     */
    @TableField("max_amount")
    private String maxAmount;

    /**
     * 加密秘钥
     */
    @TableField("secret_key")
    private String secretKey;

    /**
     * 有效天数
     */
    @TableField("valid_days")
    private String validDays;

    /**
     * 状态：1正常 0冻结
     */
    @TableField("device_status")
    private String deviceStatus;

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
    @TableField("create_time")
    private Date createTime;

}
