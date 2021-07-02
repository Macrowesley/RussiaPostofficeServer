package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatisticsDTO {
    /**
     * format: 'uuid'
     */
    String id;

    /**
     * format: 'uuid'
     * example: 163
     */
    String contractCode;

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

    TransactionDataDTO[] transactions;

    FrankDTO[] franks;
}
