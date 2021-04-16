package cc.mrbird.febs.asu.dto.service;

import cc.mrbird.febs.asu.enums.FMStatusEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangeStatusRequest {
    FMStatusEnum status;
    String postOffice;
}
