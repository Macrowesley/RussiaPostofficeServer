package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("rcs_ad_image")
public class AdImage {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;



    /**
     * frank_machine_id
     */
    @TableField("frank_machine_id")
    private String frankMachineId;


    /**
     * image_path
     */
    @TableField("image_path")
    private String imagePath;

    /**
     * 0 机器未下载成功 1机器成功下载图片
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
