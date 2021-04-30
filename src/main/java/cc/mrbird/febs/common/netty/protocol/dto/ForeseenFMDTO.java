package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseenFMDTO {
    String contractId;
    String frankMachineId;
    String foreseenId;
    String userId;
    String postOffice;
    Integer totalCount;
    ForeseenProductDTO[] products;
    String taxVersion;
    String mailVal;
}
