package cc.mrbird.febs.asu.entity.service;

import cc.mrbird.febs.asu.entity.enums.FMStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangeStatusRequest {
    FMStatus status;
    String postOffice;
}
