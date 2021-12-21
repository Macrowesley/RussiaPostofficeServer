package cc.mrbird.febs.rcs.dto.manager;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@JSONType(orders={"id","postOffice","userId","frankMachineId","contractCode","contractId","totalCount","totalAmount","taxVersion","products"})
public class ForeseenDTO {
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
    String contractId;

    @NotNull
    Integer totalCount;

    @NotNull
    ForeseenProductDTO[] products;

    String frankMachineId;

    /**
     * example:  "A0042015A"
     */
    String taxVersion;

    Double totalAmount;

    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
    Integer printJobType;

    /**
     * printJob的id
     */
    Integer printJobId;
}
