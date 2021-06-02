package cc.mrbird.febs.rcs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 合同表 Entity
 *
 * @author mrbird
 * @date 2021-04-17 14:45:52
 */
@Data
@TableName("rcs_post_office")
public class PostOffice {

    /**
     * 城市名
     */
    @TableField("city")
    private String city;


    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 邮局索引（id）
     */
    @TableId(value = "id")
    private String id;

    /**
     * 合法地址【待定:长度】
     */
    @TableField("legal_address")
    private String legalAddress;

    /**
     * 邮局名
     */
    @TableField("name")
    private String name;

    /**
     * 公司地址【待定:长度】
     */
    @TableField("office_address")
    private String officeAddress;

    /**
     * 【待定】关税区
     */
    @TableField("tariff_zone")
    private Integer tariffZone;

    /**
     * 【待定】
     */
    @TableField("time_zone")
    private Integer timeZone;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
