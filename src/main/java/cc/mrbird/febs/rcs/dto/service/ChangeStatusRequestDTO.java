package cc.mrbird.febs.rcs.dto.service;

import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangeStatusRequestDTO {
    FMStatusEnum status;
    String postOffice;
}
