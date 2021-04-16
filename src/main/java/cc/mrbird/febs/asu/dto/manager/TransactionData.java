package cc.mrbird.febs.asu.dto.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionData {
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
