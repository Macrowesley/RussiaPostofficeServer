package cc.mrbird.febs.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@Excel
public class OrderVo extends Order {

    /**
     * 设备昵称
     */
    @ExcelField("设备昵称")
    String nickname;
    /**
     * 审核类型 1 请求注资  2 请求闭环
     */
    String auditType;

    /**
     * 申请人
     */
    @ExcelField("申请人")
    String applyUserName;

    /**
     * 审核人
     */
    @ExcelField("审核人")
    Long auditUserName;

    /**
     * 闭环人
     */
    @ExcelField("闭环人")
    Long closeUserName;

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
                ", deviceId=" + deviceId +
                ", orderNumber='" + orderNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", applyUserId=" + applyUserId +
                ", auditUserId=" + auditUserId +
                ", closeUserId=" + closeUserId +
                ", expireDays=" + expireDays +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}
