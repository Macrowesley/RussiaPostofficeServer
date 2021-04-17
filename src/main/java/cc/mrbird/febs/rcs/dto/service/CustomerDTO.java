package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerDTO {
    /**
     * format: uuid
     */
    String id;

    String name;

    String inn_ru;

    String kpp_ru;
}
