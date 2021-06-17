package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatusFMDTO {
    String frankMachineId;
    String postOffice;
    String taxVersion;
    int status;
    //1 状态改变操作  2 税率表更新操作
    int event;
    //1 机器主动改变状态  2 机器被动改变状态  机器主动改变状态，这个值为1 ，如果机器收到服务器的0xC3协议，要求改变状态，改变状态后，把信息给服务器，同时，这个值为2
    int isAuto;
}
