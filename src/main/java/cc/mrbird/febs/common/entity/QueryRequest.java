package cc.mrbird.febs.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@ApiModel(description = "QueryRequest")
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    /**
     * 当前页面数据量
     */
    @ApiModelProperty("当前页面数据量")
    private int pageSize = 10;

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private int pageNum = 1;

    /**
     * 排序字段
     */
    @ApiModelProperty(name="field",  hidden = true)
    private String field;

    /**
     * 排序规则，asc升序，desc降序
     */
    @ApiModelProperty(name="order",  hidden = true)
    private String order;
}
