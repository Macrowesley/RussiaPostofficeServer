package cc.mrbird.febs.rcs.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 数据同步记录表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:00
 */
@Data
@TableName("rcs_balance")
public class Balance {

    /**
     * balance主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 当前余额
     */
    @TableField("consolidate")
    private String consolidate;

    /**
     * 【待定】当前可用资金
     */
    @TableField("contract_current")
    private String contractCurrent;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 合同号
     */
    @TableField("contract_num")
    private Integer contractNum;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 流程 0 未闭环   1 闭环(成功后闭环) -1 闭环(失败后闭环)
     */
    @TableField("flow")
    private Integer flow;

    /**
     * 1 从机器发往俄罗斯服务器的数据  2 从俄罗斯服务器返回的数据
     */
    @TableField("from_type")
    private Integer fromType;



    /**
     * 【待定】
     */
    @TableField("operation_id")
    private String operationId;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
