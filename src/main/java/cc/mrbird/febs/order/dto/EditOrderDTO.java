package cc.mrbird.febs.order.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditOrderDTO {
    @NotNull
    @Min(1)
    Long orderId;


    @NotBlank
    String amount;

    @NotNull
    @Min(1)
    Integer expireDays;
}
