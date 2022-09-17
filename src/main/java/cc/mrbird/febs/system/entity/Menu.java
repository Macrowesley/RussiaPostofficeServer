package cc.mrbird.febs.system.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 8571011372410167901L;

    /**
     *  菜单
     */
    public static final String TYPE_MENU = "0";
    /**
     * 按钮
     */
    public static final String TYPE_BUTTON = "1";

    public static final Long TOP_NODE = 0L;

    /**
     * 菜单/按钮ID
     */
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Long menuId;

    /**
     * 上级菜单ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

//    /**
//     * 菜单/按钮名称
//     */
//    @TableField("MENU_NAME")
//    @NotBlank(message = "{required}")
//    @Size(max = 10, message = "{noMoreThan}")
//    @ExcelProperty(value = "名称")
//    private String menuEnglishName;


    /**
     * 菜单/按钮名称
     */
    @TableField("MENU_CHINESE_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 30, message = "{noMoreThan}")
    @ExcelProperty(value = "中文名称")
    private String menuChineseName;

    /**
     * 菜单/按钮名称
     */
    @TableField("MENU_RUSSIAN_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelProperty(value = "俄文名称")
    private String menuRussianName;

    /**
     * 菜单/按钮名称
     */
    @TableField("MENU_ENGLISH_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelProperty(value = "英文名称")
    private String menuEnglishName;

    /**
     * 菜单URL
     */
    @TableField("URL")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelProperty(value = "URL")
    private String url;

    /**
     * 权限标识
     */
    @TableField("PERMS")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelProperty(value = "权限")
    private String perms;

    /**
     * 图标
     */
    @TableField("ICON")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelProperty(value = "图标")
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    @TableField("TYPE")
    @NotBlank(message = "{required}")
    private String type;

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

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;


}
