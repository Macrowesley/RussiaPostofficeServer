package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 预算订单 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:44:51
 */
@Data
@TableName("rcs_foreseen")
public class Foreseen {
    /**
     * 主键uuid
     */
    @TableId(value = "id")
    private String id;

    /**
     * 邮局信息
     */
    @TableField("post_office")
    private String postOffice;

    @TableField("user_id")
    String userId;

    /**
     * 合同id 8位数字
     */
    @TableField("contract_code")
    private String contractCode;


    /**
     * 0 流程中 1 闭环
     */
    @TableField("foreseen_status")
    private Integer foreseenStatus;


    /**
     * 总数量
     */
    @TableField("total_count")
    private Integer totalCount;

    /**
     * 机器id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;

    /**
     * tax版本
     */
    @TableField("tax_version")
    private String taxVersion;


    /**
     * 【待定】
     */
    @TableField("total_ammount")
    private Double totalAmmount;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
