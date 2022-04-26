package cc.mrbird.febs.rcs.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Msg批次信息
 */
@Data
@ToString
public class TransactionMsgBatchInfo {
    Date startDate;
    Date endDate;
    String taxRegionType;
    String taxLabelRu;
    Integer foreseenOneBatchCount;
    Integer foreseenOneBatchWeight;
    Integer taxFixedValue;
    /**
     * foreseenOneBatchCount * taxFixedValue
     */
    Double callculationAmount;
    Integer transactionOneBatchCount;
    Integer transactionOneBatchWeight;
    /**
     *  = taxFixedValue
     */
    Integer taxRealSaleRate;

    /**
     * 额外信息，用于辅助，不用于excel
     */
    String code;
    Long startMsgId;

}
