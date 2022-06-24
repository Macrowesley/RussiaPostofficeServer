package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 清空机器累计金额记录 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
@Data
@ToString
@TableName("rcs_clearing_record")
public class ClearingRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 用户id
     */
    @TableId(value = "userId")
    private Long userId;


    /**
     * 用户名
     */
    @TableField("username")
    private String username;


    /**
     * 机器累计总金额 单位是分
     */
    @TableField("total_money")
    private Long totalMoney;

    /**
     * 清空原因
     */
    @TableField("reason")
    private String reason;


    /**
     * 0 未成功 1成功清零
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
