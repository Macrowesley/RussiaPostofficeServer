package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class AddressDTO {
    String address;
    String imageName;
    /**
     * 1 文本地址 对应上面的address
     * 2 图片类型 对应上面的imageName
     */
    String type;
}
