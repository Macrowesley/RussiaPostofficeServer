package cc.mrbird.febs.rcs.dto.ui;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel
@AllArgsConstructor
public class PrintJobReq {
    public PrintJobReq(){

    }

    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 机器id
     */
    private String frankMachineId;

    private String contractCode;

    double totalAmount;

    @ApiModelProperty(hidden = true)
    int totalCount;

    List<ForeseenProductFmDto> products;

    @ApiModelProperty(hidden = true)
    private String foreseenId;

    @ApiModelProperty(hidden = true)
    private String transactionId;

    /**
     * 打印对象类型：过戳还是签条
     * 1 stamp
     * 2 stick
     */
    @ApiModelProperty(notes = "打印对象类型：过戳还是签条 1 stamp 2 stick")
    private int printObjectType;

}
