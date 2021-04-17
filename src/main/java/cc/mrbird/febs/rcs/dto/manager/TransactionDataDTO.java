package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionDataDTO {
    /**
     * format: 'date-time'
     */
    String dateTime;

    String productCode;

    int weight;

    int count;

    double amount;

    String taxVersion;
}
