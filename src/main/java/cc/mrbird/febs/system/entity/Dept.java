package cc.mrbird.febs.system.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_dept")
@ApiModel
public class Dept implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;

    public static final Long TOP_NODE = 0L;

    /**
     * 部门 ID
     */
    @TableId(value = "DEPT_ID", type = IdType.AUTO)
    private Long deptId;

    /**
     * 上级部门 ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 部门名称
     */
    @TableField("DEPT_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private Long orderNum;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("MODIFY_TIME")
    private Date modifyTime;

}
