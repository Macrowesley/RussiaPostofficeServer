package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ContractDTO {
    /**
     * 8位数字
     */
    String code;

    String name;

    boolean enable;

    CustomerDTO customer;

    PostOfficeDTO[] postOffices;

    String modified;
}
