package cc.mrbird.febs.device.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class UpdateDeviceDTO {

    @NotNull
    @Min(1)
    Long deviceId;

    @NotBlank
    String acnum;

    @NotBlank
    String nickname;

    @NotBlank
    String warnAmount;

    @NotBlank
    String maxAmount;

    @NotNull
    @Min(1)
    Integer validDays;

    @NotBlank
    String deviceStatus;

    @NotNull
    @Min(1)
    Long bindUserId;

    @NotNull
    @Min(1)
    Long userDeviceId;
}
