package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class ForeseenProductDTO {
    @NotBlank
    String productCode;

    @NotNull
    Integer count;

    @NotNull
    Double weight;

    @NotNull
    Double amount;
}
