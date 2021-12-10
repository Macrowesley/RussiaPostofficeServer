package cc.mrbird.febs.common.netty.protocol.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

/**
 * PC点击打印按钮后发给机器的信息
 */
@Data
@ToString
public class PcPrintInfoDTO {
    Integer printJobId;
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
    PcPrintProductDTO[] printProducts;
}
