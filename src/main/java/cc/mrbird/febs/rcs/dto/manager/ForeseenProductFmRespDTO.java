package cc.mrbird.febs.rcs.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 返回给机器的产品详情
 */
@Data
@ToString
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ForeseenProductFmRespDTO {
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

    String adImageName;

    String adImagePath;

    /**
     * 地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id
     */
    String type;

    /**
     * 已经打印数量
     */
    Integer alreadyPrintCount = 0;
}
