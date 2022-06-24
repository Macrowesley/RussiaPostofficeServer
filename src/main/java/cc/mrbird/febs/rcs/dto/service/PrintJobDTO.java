package cc.mrbird.febs.rcs.dto.service;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
@ApiModel
public class PrintJobDTO {
    @ApiModelProperty(hidden = true)
    private Integer id;
    private String contractCode;
    private String foreseenId;
    private String transactionId;
    @ApiModelProperty(hidden = true)
    String userId;
    @ApiModelProperty(hidden = true)
    Integer pcUserId;
    private String frankMachineId;
    @ApiModelProperty(hidden = true)
    private Integer flow;
    @ApiModelProperty(hidden = true)
    private Integer flowDetail;
    @ApiModelProperty(hidden = true)
    int totalCount;
    @ApiModelProperty(hidden = true)
    Double totalAmount;
    @ApiModelProperty(hidden = true)
    Double contractCurrent;
    @ApiModelProperty(hidden = true)
    Double contractConsolidate;
    @ApiModelProperty(hidden = true)
    private Integer type;
    @ApiModelProperty(hidden = true)
    private Integer cancelMsgCode;
    @ApiModelProperty(hidden = true)
    private Date updatedTime;
    @ApiModelProperty(hidden = true)
    private Date createdTime;
    @ApiModelProperty(hidden = true)
    private String startDate;
    @ApiModelProperty(hidden = true)
    private String endData;
}
