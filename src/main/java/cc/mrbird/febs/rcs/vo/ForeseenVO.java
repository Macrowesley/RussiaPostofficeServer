package cc.mrbird.febs.rcs.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 *
 * foreseen实体
 */

@Data
@ToString
public class ForeseenVO {
    private String id;
    private String postOffice;
    String userId;
    private String contractCode;
    private Integer foreseenStatus;
    private Integer totalCount;
    private String frankMachineId;
    private String taxVersion;
    private Double totalAmmount;
    private Date updatedTime;
    private Date createdTime;
}
