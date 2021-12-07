package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Pc发给机器的打印订单中商品的信息
 */
@Data
@ToString
public class PcPrintProductDTO {
    String code;
    Integer count;
    /**
     * 全整数，自己除以100，添加小数点
     */
    String weight;
    String address;
    /**
     * 打印数量
     */
    Integer printCount;
}
