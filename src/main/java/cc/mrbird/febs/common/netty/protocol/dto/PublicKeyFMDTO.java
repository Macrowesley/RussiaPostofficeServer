package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PublicKeyFMDTO {
    String frankMachineId;
    String publicKey;
    String privateKey;
}
