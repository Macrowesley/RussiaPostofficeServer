package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CheckServiceDTO {
    String frankMachineId;
    String taxVersion;
    String machineDate;
    TransactionMsgFMDTO dmMsgDto;
}
