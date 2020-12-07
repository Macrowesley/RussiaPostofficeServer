package cc.mrbird.febs.device.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class AddDeviceDTO {

    @NotBlank
    String warnAmount;

    @NotBlank
    String maxAmount;

    @NotNull
    @Min(1)
    Integer validDays;

    @NotBlank
    String deviceStatus;

    @NotBlank
    @Length(min = 6)
    String acnumList;

    @NotNull
    @Min(1)
    Long bindUserId;
}
