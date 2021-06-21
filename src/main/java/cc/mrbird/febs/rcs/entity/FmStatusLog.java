package cc.mrbird.febs.rcs.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 机器状态变更表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:44:22
 */
@Data
@TableName("rcs_fm_status_log")
public class FmStatusLog {

    /**
     * 提交改变方：1 机器 2 俄罗斯
     */
    @TableField("change_from")
    private Integer changeFrom;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 机器当前状态
     */
    @TableField("cur_fm_status")
    private Integer curFmStatus;

    /**
     * 机器想要达到的状态
     */
    @TableField("future_fm_status")
    private Integer futureFmStatus;

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
     * 流程
     */
    @TableField("flow")
    private Integer flow;

    /**
     * 各种FlowXXXEnum的状态
     */
    @TableField("flow_detail")
    private Integer flowDetail;

    /**
     * 1 STATUS 
									 2 RATE_TABLE_UPDATE
     */
    @TableField("fm_event")
    private Integer fmEvent;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;



    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 接口名
     */
    @TableField("interface_name")
    private Integer interfaceName;

    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;

    /**
     * tax版本
     */
    @TableField("tax_version")
    private String taxVersion;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
