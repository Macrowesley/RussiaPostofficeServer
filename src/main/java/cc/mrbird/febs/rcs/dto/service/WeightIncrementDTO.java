package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"after","every","value"})
public class WeightIncrementDTO {
    int after;
    int every;
    double value;
}
