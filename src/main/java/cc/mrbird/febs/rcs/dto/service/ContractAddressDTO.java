package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class ContractAddressDTO {
    String contractCode;
    String fmId;
    String addressList;
}
