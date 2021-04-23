package cc.mrbird.febs.common.netty.protocol.machine.DTO;

import cc.mrbird.febs.rcs.entity.PostOffice;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatusDTO {
    String version;
    String frankMachineId;
    String postOffice;
    String taxVersion;
    int status;
    //1 状态改变操作  2 税率表更新操作
    int event;
    //不是lost请求 1是lost请求
    int isLost;
}
