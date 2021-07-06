package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"from","to","value"})
public class WeightedTariffDTO {
    int from;
    int to;
    double value;
}
