package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class ContractAddressDTO {
    @NotBlank
    String contractId;
    @NotBlank
    String FMid;
    @NotBlank
    String addressList;
}
