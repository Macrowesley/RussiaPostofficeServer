package cc.mrbird.febs.rcs.dto.ui;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
public class PrintJobAddDto {

    private Integer id;

    private String contractCode;

    private String foreseenId;

    private String transactionId;

    String userId;

    /**
     * 机器id
     */
    private String frankMachineId;

    /**
     * 整个流程状态
     */
    private Integer flow;

    private Integer flowDetail;

    /**
     * 取消原因
     */
    private Integer cancelMsgCode;

    private Date updatedTime;

    private Date createdTime;

    int totalCount;

    ArrayList<PrintJobProductDto> products;
}
