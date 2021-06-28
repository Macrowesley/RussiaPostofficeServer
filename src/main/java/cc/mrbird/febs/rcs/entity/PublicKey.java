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
     * 机器id
     */
    @TableId("frank_machine_id")
    private String frankMachineId;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 私钥
     */
    @TableField("private_key")
    private String privateKey;

    /**
     * 整个流程状态
     */
    @TableField("flow")
    private Integer flow;

    @TableField("flow_detail")
    private Integer flowDetail;


    /**
     * 公钥的版本（序列号）
     */
    @TableField("revision")
    private Integer revision;

    @TableField("alg")
    private String alg;


    /**
     * 获取返回给机器的publickey信息
     * @return
     */
    public String getSuccessMsg() {
//        return String.format("%04d",this.getRevision())  + this.getPrivateKey();
        //todo 服务器和机器的ECDSA算法不一致，暂时以机器的私钥公钥为准
        return String.format("%04d",this.getRevision());
    }
}
