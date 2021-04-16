package cc.mrbird.febs.asu.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostOffice {
    /**
     * 邮局索引（id）
     */
    String index;

    String name;

    String city;

    String legalAddress;

    String officeAddress;

    int timeZone;

    int tariffZone;
}
