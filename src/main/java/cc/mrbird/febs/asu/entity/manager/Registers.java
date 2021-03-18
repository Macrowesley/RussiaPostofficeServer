package cc.mrbird.febs.asu.entity.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Registers {
    /**
     * format: uuid
     */
    String id;

    /**
     * format: uuid
     * example: 163
     */
    String contractId;

    @Deprecated
    Integer contractNum;

    String postofficeIndex;

    String frankMachineId;

    /**
     * format: 'date-time'
     */
    String timestamp;

    /**
     * example: 100.50
     */
    Double ascRegister;

    Double decRegister;

    double amount;

    /**
     * enum:
     * - REFILL
     * - REFUND
     */
    String type;


}
