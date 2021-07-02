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
     * 合同id
     */
    @TableField("contract_code")
    private String contractCode;


    /**
     *
     */
    @TableField("contract_current")
    private Double current;

    /**
     * 当前余额
     */
    @TableField("consolidate")
    private Double consolidate;


    /**
     *
     */
    @TableField("operation_id")
    private String operationId;

    /**
     * 1 服务器发送给俄罗斯，俄罗斯返回的信息  2 俄罗斯发给服务器的数据
     */
    @TableField("from_type")
    private Integer fromType;


    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 俄罗斯发来的时间 timestamp 和 modified
     */
    @TableField("updated_time")
    private Date russiaTime;

}
