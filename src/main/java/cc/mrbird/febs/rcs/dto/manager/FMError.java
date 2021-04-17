package cc.mrbird.febs.rcs.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
public class FMError {
    /**
     * 1000
     */
    @NotBlank
    String code;
    String message;
}
