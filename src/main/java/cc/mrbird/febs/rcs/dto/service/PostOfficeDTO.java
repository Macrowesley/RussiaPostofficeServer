package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostOfficeDTO {
    /**
     * 邮局索引（id）
     */
    String index;

    String name;

    String city;

    int timeZone;

    int tariffZone;

    String modified;
}
