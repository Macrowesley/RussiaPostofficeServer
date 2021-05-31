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
}
