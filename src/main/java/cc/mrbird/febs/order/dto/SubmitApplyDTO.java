package cc.mrbird.febs.order.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubmitApplyDTO {
    @NotNull
    @Min(1)
    Long orderId;

    @Length(max = 100)
    String submitInfo;

    @NotBlank
    String auditType;
}
