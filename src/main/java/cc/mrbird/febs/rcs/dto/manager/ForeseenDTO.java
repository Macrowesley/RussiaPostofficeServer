package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
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

    @NotNull
    Integer totalCount;

    @NotNull
    ForeseenProductDTO[] products;

    String frankMachineId;

    /**
     * example:  "A0042015A"
     */
    String taxVersion;

    Double totalAmmount;
}
