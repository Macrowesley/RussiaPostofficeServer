package cc.mrbird.febs.rcs.dto.manager;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发给俄罗斯的foreseen数据
 */
@Data
@ToString
@JSONType(orders={"id","postOffice","userId","frankMachineId","contractCode","contractId","totalCount","totalAmount","taxVersion","products"})
public class ForeseenRussiaDTO {
    /**
     * format: uuid
     */
    @NotBlank
    String id;

    @NotBlank
    String postOffice;

    @NotBlank
    String userId;

    /**
     * 8位数字
     */
    String contractCode;

    @NotNull
    Integer totalCount;

    @NotNull
    ForeseenProductRussiaRespDTO[] products;

    String frankMachineId;

    /**
     * example:  "A0042015A"
     */
    String taxVersion;

    Double totalAmount;

    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
//    Integer printJobType;

    /**
     * printJob的id
     */
//    Integer printJobId;
}
