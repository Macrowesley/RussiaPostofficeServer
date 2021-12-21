package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CancelPrintResDto {
    String printJobId;
    // 0 失败 1 成功
    String res;
}
