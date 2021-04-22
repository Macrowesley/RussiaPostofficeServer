package cc.mrbird.febs.rcs.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NonNull;
import lombok.ToString;

/**
 * 合同表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
@Data
@ToString
@TableName("rcs_contract")
public class Contract {

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 【待定】0 不可用 1 可用
     */
    @TableField("enable")
    private String enable;

    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 数量
     */
    @TableField("num")
    private String num;

    /**
     * 当前可用资金（包括持有）
     */
    @TableField("current")
    private String current;

    /**
     * 当前余额（仅事实）
     */
    @TableField("consolidate")
    private String consolidate;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
