package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 未收到tax的device统统记录在这里
 */
@Data
@TableName("rcs_tax_device_unreceived")
public class TaxDeviceUnreceived {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("fm_id")
    private String frankMachineId;

    @TableField("tax_version")
    private String taxVersion;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
