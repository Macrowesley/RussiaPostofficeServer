package cc.mrbird.febs.rcs.req;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AdImageAddReq {
    @NotBlank
    String frankMachineId;
    @NotBlank
    String url;
}
