package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

/**
 * PC点击取消按钮，发送给机器的信息
 */
@Data
@ToString
public class PcCancelInfoDTO {
    String foreseenId;
}
