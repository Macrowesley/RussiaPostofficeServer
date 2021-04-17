package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 机器信息 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:05
 */
@Data
@TableName("rcs_frank_machine")
public class FrankMachine {

    /**
     * 【待定】表头号
     */
    @TableField("acnum")
    private String acnum;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 【待定】机器当前状态
												1 ENABLED
												2 DEMO
												3 BLOCKED
												4 UNAUTHORIZED
												5 LOST
     */
    @TableField("cur_fm_status")
    private Integer curFmStatus;

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
     * 流程 0 未闭环   1 闭环(成功后闭环) -1 闭环(失败后闭环)
     */
    @TableField("flow")
    private Integer flow;

    /**
     * 1 STATUS 
									 2 RATE_TABLE_UPDATE
     */
    @TableField("fm_event")
    private Integer fmEvent;

    /**
     * 机器想要达到的状态
     */
    @TableField("future_fm_status")
    private String futureFmStatus;

    /**
     * 机器主键uuid
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

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

    /**
     * 【待定】
     */
    @TableField("user_id")
    private String userId;

}
