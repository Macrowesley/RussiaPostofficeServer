package cc.mrbird.febs.device.vo;

import lombok.Data;

@Data
public class UserDeviceVO {
    private Long deviceId;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    private String username;

    private String realname;
}
