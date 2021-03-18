package cc.mrbird.febs.asu.entity.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Contract {
    /**
     * format: uuid
     */
    String id;

    String num;

    boolean enable;

    Customer customer;

    PostOffice[] postOffice;
}
