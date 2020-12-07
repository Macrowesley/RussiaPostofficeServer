package cc.mrbird.febs.device.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CheckIsExistDTO {
    @NotBlank
    @Length(min = 6)
    String acnumList;
}
