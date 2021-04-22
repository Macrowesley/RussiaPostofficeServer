package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionDTO {

    /**
     * format: uuid
     */
    String id;

    /**
     * format: uuid
     */
    String foreseenId;

    String postOffice;

    String frankMachineId;

    /**
     * format: uuid
     */
    String contractId;

    @Deprecated
    Integer contractNum;

    /**
     * format: 'date-time'
     */
    String startDateTime;

    /**
     * format: 'date-time'
     */
    String stopDateTime;

    String userId;

    Double creditVal;

    Double amount;

    int count;

    String graphId;

    /**
     * example: "A0042015A"
     */
    String taxVersion;

    FrankDTO[] franks;


}