package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionFMDTO {
    String id;
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
    String amount;
    int count;
    String graphId;
    String taxVersion;
    FrankDTO[] franks;

    //具体什么数字代表什么原因，以后扩展，目前默认为1，理由：得想一个
    int cancelMsgCode;
}
