package cc.mrbird.febs.rcs.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * PC发过来的产品信息
 */
@Data
@ToString
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ForeseenProductPcReqDTO {
    String productCode;

    /**
     * 订单id
     */
    @ApiModelProperty(hidden = true)
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

    String address;

    String adImageId;

    /**
     * 地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id
     */
    @ApiModelProperty(notes = "地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id")
    Integer type;

    /**
     * 已经打印数量
     */
    @ApiModelProperty(hidden = true)
    Integer alreadyPrintCount = 0;
}
