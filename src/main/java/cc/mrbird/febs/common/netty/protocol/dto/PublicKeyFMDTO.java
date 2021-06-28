package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PublicKeyFMDTO {
    String result;//(1成功 0 失败)
    String frankMachineId;
    String publicKey;
    String privateKey;
}
