package cc.mrbird.febs.order.entity;

import cc.mrbird.febs.common.enums.OrderBtnEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Excel("注资报表")
public class OrderVo extends Order {

    /**
     * 表头号
     */
    @ExcelField(value = "表头号")
    String acnum;

    /**
     * 设备昵称
     */
    @ExcelField(value = "设备昵称")
    String nickname;
    /**
     * 审核类型 1 请求注资  2 请求闭环
     */
    String auditType;

    /**
     * 申请人
     */
    @ExcelField(value = "申请人")
    String applyUserName;

    /**
     * 审核人
     */
    @ExcelField(value = "审核人")
    String auditUserName;

    /**
     * 闭环人
     */
//    @ExcelField("闭环人")
    String closeUserName;

    /**
     * 这个订单的操作列表
     */
    List<Map<String, String>> btnList = new ArrayList<>();

    @Override
    public String toString() {
        return "OrderVo{" +
                "acnum='" + acnum + '\'' +
                ", nickname='" + nickname + '\'' +
                ", auditType='" + auditType + '\'' +
                ", applyUserName='" + applyUserName + '\'' +
                ", auditUserName=" + auditUserName +
                ", closeUserName=" + closeUserName +
                ", orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", applyUserId=" + applyUserId +
                ", auditUserId=" + auditUserId +
                ", closeUserId=" + closeUserId +
                ", expireDays=" + expireDays +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", btnList=" + btnList.toString() +
                '}';
    }
}
