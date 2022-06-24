package cc.mrbird.febs.rcs.dto.ui;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "清除金额记录实体类")
public class ClearRecordReq {

    /**
     * 机器累计总金额 单位是分
     */
    @ApiModelProperty("机器累计总金额")
    private String totalMoney;

    /**
     * 清空原因
     */
    @ApiModelProperty(name="清空原因",  hidden = true)
    private String reason;
}
