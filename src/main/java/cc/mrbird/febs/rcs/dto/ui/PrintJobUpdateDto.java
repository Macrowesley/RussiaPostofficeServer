package cc.mrbird.febs.rcs.dto.ui;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.Count;

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

    ArrayList<ForeseenProductDTO> products;

    private String foreseenId;

    private String transactionId;
}
