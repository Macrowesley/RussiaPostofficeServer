package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import lombok.Data;
import lombok.ToString;

/**
 * PC点击打印按钮后发给机器的信息
 */
@Data
@ToString
public class PcPrintInfoDTO {
    Integer printJobId;
    //pc用户ID
    String pcUserId;
    String frankMachineId;
    private String foreseenId;
    private String transactionId;
    String contractCode;
    Integer flowDetail;
    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
    Integer printJobType;
    /**
     * 打印商品列表
     */
    ForeseenProductFmDto[] printProducts;
}
