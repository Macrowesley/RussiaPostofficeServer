package cc.mrbird.febs.rcs.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 邮局-合同关系表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:46:07
 */
@Data
@TableName("rcs_post_office_contract")
public class PostOfficeContract {

    /**
     * 合同id
     */
    @TableField("contract_code")
    private String contractCode;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮局id
     */
    @TableField("post_office_id")
    private String postOfficeId;

}
