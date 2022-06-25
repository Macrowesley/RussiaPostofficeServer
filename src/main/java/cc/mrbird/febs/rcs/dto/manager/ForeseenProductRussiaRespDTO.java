package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

/**
 * 返回给俄罗斯的产品信息
 */
@Data
@ToString
public class ForeseenProductRussiaRespDTO {
    String productCode;


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
