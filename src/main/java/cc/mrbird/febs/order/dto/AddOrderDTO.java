package cc.mrbird.febs.order.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddOrderDTO {
    @NotNull
    @Min(1)
    Long deviceId;

    @NotNull
    @Min(1)
    Long auditUserId;

    @NotBlank
    String amount;

    @NotNull
    @Min(1)
    Integer expireDays;
}
