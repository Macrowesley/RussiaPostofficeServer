package cc.mrbird.febs.rcs.dto.ui;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmReqDTO;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

/**
 *
 */
@Data
@ToString
public class PrintJobUpdateDto {
    private Integer id;

    /**
     * 机器id
     */
    private String frankMachineId;

    private String contractCode;

    int totalCount;

    Double totalAmount;

    ArrayList<ForeseenProductFmReqDTO> products;

    private String foreseenId;

    private String transactionId;
}
