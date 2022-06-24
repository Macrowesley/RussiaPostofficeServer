package cc.mrbird.febs.rcs.dto.manager;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel
public class ForeseenProductFmDto {
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

    /**
     * 地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id
     */
    @ApiModelProperty(notes = "地址栏采用哪种内容： 1 文本地址 address  2 广告图片地址id")
    String type;

    /**
     * 已经打印数量
     */
    @ApiModelProperty(hidden = true)
    Integer alreadyPrintCount = 0;
}
