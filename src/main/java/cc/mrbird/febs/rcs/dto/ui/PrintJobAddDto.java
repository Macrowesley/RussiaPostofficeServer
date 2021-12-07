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

    /**
     * 机器id
     */
    private String frankMachineId;

    private String contractCode;

    int totalCount;

    ArrayList<PrintJobProductDto> products;

    private String foreseenId;

    private String transactionId;

}
