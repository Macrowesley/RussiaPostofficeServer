package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * PC点击取消按钮，发送给机器的信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PcCancelInfoDTO {
    String foreseenId;
}
