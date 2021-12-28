package cc.mrbird.febs.rcs.dto.ui;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author Administrator
 */
@Data
@ToString
public class PrintJobAddDto {

    private Integer id;

    /**
     * 机器id
     */
    private String frankMachineId;

    private String contractCode;

    double totalAmount;
    int totalCount;

    ArrayList<ForeseenProductFmDto> products;

    private String foreseenId;

    private String transactionId;

}
