package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 公钥表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:23
 */
@Data
@TableName("rcs_public_key")
public class PublicKey {

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 到期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 流程 0 未闭环   1 闭环(成功后闭环) -1 闭环(失败后闭环)
     */
    @TableField("flow")
    private Integer flow;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 公钥的版本（序列号）
     */
    @TableField("revision")
    private String revision;

}
