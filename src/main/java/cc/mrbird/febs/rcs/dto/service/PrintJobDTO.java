package cc.mrbird.febs.rcs.dto.service;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class PrintJobDTO {
    private Integer id;
    private String contractCode;
    private String foreseenId;
    private String transactionId;
    String userId;
    Integer pcUserId;
    private String frankMachineId;
    private Integer flow;
    private Integer flowDetail;
    int totalCount;
    Double totalAmount;
    Double contractCurrent;
    Double contractConsolidate;
    private Integer type;
    private Integer cancelMsgCode;
    private Date updatedTime;
    private Date createdTime;
    private String startDate;
    private String endData;
}
