package cc.mrbird.febs.asu.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Customer {
    /**
     * format: uuid
     */
    String id;

    String name;

    String inn_ru;

    String kpp_ru;
}
