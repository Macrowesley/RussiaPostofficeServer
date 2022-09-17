package cc.mrbird.febs.device.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendDeviceDTO {
    @NotBlank
    String deviceIds;

    @NotNull
    @Min(1)
    Long bindUserId;
}
