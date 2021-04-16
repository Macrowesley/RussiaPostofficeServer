package cc.mrbird.febs.asu.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Statistics {
    /**
     * format: 'uuid'
     */
    String id;

    /**
     * format: 'uuid'
     * example: 163
     */
    String contractId;

    @Deprecated
    int contractNum;

    String postOffice;

    String frankMachineId;

    /**
     * format: 'date-time'
     */
    String frankingPeriodStart;

    /**
     * format: 'date-time'
     */
    String frankingPeriodEnd;

    double totalAmount;

    long initialPieceCounter;

    long finalPieceCounter;

    TransactionData[] transactions;

    Frank[] franks;
}
