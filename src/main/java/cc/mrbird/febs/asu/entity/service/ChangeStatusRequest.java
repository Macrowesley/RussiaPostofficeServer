package cc.mrbird.febs.asu.entity.service;

import cc.mrbird.febs.asu.entity.enums.FMStatusEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangeStatusRequest {
    FMStatusEnum status;
    String postOffice;
}
