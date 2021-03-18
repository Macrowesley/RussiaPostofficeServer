package cc.mrbird.febs.asu.entity.manager;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class ForeseenProduct {
    @NotBlank
    String productCode;

    @NotNull
    Integer count;

    @NotNull
    Double weight;

    @NotNull
    Double amount;
}
