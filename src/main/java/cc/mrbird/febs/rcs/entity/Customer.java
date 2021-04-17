package cc.mrbird.febs.rcs.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:59
 */
@Data
@TableName("rcs_customer")
public class Customer {

    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 【待定】
     */
    @TableField("inn_ru")
    private String innRu;

    /**
     * 【待定】
     */
    @TableField("kpp_ru")
    private String kppRu;

    /**
     * 【待定】
     */
    @TableField("name")
    private String name;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
