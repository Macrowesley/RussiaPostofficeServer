package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionFMDTO {
    String foreseenId;
    String postOffice;
    String frankMachineId;
    String contractId;
    /**
     * 花费时间，单位；分
     */
    String costTime;
    String userId;
    String creditVal;
    String mailVal;
    int count;
    String graphId;
    String taxVersion;
    FrankDTO[] franks;
}
