package cc.mrbird.febs.rcs.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 返回给PC的产品信息
 */
@Data
@ToString
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ForeseenProductPcRespDTO {
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

    String address;

    String adImageId;

    String adImagePath;

    /**
     * 地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id
     */
    @ApiModelProperty(notes = "地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id")
    String type;


}
