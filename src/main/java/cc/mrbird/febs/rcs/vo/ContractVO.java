package cc.mrbird.febs.rcs.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 合同表 Entity
 */
@Data
@ToString
public class ContractVO {

    private String id;
    private String num;
    private Integer enable;
    private Date createdTime;
    private Double current;
    private Double consolidate;
    private Date updatedTime;
    private String addressContent;
}
