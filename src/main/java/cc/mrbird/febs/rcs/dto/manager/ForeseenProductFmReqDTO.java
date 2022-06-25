package cc.mrbird.febs.rcs.dto.manager;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 机器发过来的产品信息
 */
@Data
@ToString
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ForeseenProductFmReqDTO {
    String productCode;

    /**
     * 订单id
     */
    Integer printJobId;

    /**
     * 打印总数量
     */
    Integer count;

    /**
     * 全整数，自己除以100，添加小数点
     */
    Double weight;

    Double amount;
}
