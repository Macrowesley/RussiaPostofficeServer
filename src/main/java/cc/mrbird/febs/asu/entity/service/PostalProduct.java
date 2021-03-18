package cc.mrbird.febs.asu.entity.service;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ToString
public class PostalProduct {
    /**
     * example: "2000"
     */
    String id;

    String name;

    String code;

    /**
     * 转发区域； 1-内部，2-外部
     */
    int regionType;

    /**
     * 关税区（值1-5）
     */
    @Min(1) @Max(5)
    Integer zoneCode;

    /**
     * 偏远地区； 1-长达2000公里，2-超过2000公里
     */
    @Min(1) @Max(2)
    Integer distanceType;

    /**
     * 邮政物品的最大重量
     */
    Integer maxWeight;

    /**
     #邮件类别的名称
     #*简单-0
     #*定制-1
     #*申报价值-2
     */
    @Min(0) @Max(2)
    Integer mailCtg;

    /**
     #邮件类型代码
     #* 2-字母
     #* 3-包裹邮寄
     */
    @Min(2) @Max(3)
    Integer mailType;

    /**
     #运输方向：
     #* 1-内部（RTM-2）;
     #* 2-外向国际（RTM-2）;
     */
    @Min(1) @Max(2)
    Integer directctg;

    TaxRate[] taxes;
}
