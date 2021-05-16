package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CancelJobFMDTO {
    String contractId;
    String frankMachineId;
    String foreseenId;
    //具体什么数字代表什么原因，以后扩展，目前默认为1，理由：得想一个
    int cancelMsgCode;
}
