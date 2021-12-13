package cc.mrbird.febs.rcs.dto.manager;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseenProductDTO {
    String productCode;

    /**
     * 订单id
     */
    private Integer printJobId;

    /**
     * 打印总数量
     */
    Integer count;

    /**
     * 全整数，自己除以100，添加小数点
     */
    Double weight;

    Double amount;

    String address;

    /**
     * 已经打印数量
     */
    Integer alreadyPrintCount;
}
