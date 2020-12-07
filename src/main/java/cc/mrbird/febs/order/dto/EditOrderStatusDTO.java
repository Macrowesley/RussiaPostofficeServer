package cc.mrbird.febs.order.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class EditOrderStatusDTO {
    @NotNull
    @Min(1)
    Long orderId;
}
