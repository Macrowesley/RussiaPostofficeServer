package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ContractDTO {
    /**
     * format: uuid
     */
    String id;

    String num;

    boolean enable;

    CustomerDTO customer;

    PostOfficeDTO[] postOffice;
}
