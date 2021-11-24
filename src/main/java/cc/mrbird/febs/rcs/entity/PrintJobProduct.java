package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @author Administrator
 */
@Data
@ToString
@TableName("rcs_print_job_product")
public class PrintJobProduct {
    /**
     * 自增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("print_job_id")
    private Integer printJobId;
    @TableField("code")
    String code;
    @TableField("count")
    Integer count;
    @TableField("weight")
    Double weight;
    @TableField("address")
    String address;
}
