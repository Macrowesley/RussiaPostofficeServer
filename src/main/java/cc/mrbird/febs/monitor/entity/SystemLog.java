package cc.mrbird.febs.monitor.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_log")
public class SystemLog implements Serializable {

    /**
     * 日志ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户
     */
    @TableField("USERNAME")
    @ExcelProperty(value = "操作用户")
    private String username;

    /**
     * 操作内容
     */
    @TableField("OPERATION")
    @ExcelProperty(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    @TableField("TIME")
    @ExcelProperty(value = "耗时（毫秒）")
    private Long time;

    /**
     * 操作方法
     */
    @TableField("METHOD")
    @ExcelProperty(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    @TableField("PARAMS")
    @ExcelProperty(value = "方法参数")
    private String params;

    /**
     * 操作者IP
     */
    @TableField("IP")
    @ExcelProperty(value = "操作者IP")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 操作地点
     */
    @TableField("LOCATION")
    @ExcelProperty(value = "操作地点")
    private String location;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
