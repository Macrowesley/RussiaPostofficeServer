package cc.mrbird.febs.rcs.dto.ui;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductPcReqDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductPcRespDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class PrintJobResp {

    private Integer id;

    /**
     * 机器id
     */
    private String frankMachineId;

    private String contractCode;

    double totalAmount;

    int totalCount;

    List<ForeseenProductPcRespDTO> products;

    private String foreseenId;

    private String transactionId;

    /**
     * 打印对象类型：过戳还是签条
     * 1 stamp
     * 2 stick
     */
    @ApiModelProperty(notes = "打印对象类型：过戳还是签条 1 stamp 2 stick")
    private int printObjectType;

}
