package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseenProductRussiaDto {
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
