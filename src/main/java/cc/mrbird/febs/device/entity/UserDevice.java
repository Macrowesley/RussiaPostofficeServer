package cc.mrbird.febs.device.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 用户设备关联表 Entity
 *
 * @author mrbird
 * @date 2020-05-29 14:38:03
 */
@Data
@TableName("t_user_device")
public class UserDevice implements Serializable {

    private static final long serialVersionUID = 8522719001863115656L;
    /**
     * 设备id
     */
    @TableField("device_id")
    private Long deviceId;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

}
