package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"type","fixedValue","weight","increment"})
public class TariffDTO {
    //FIXED, WEIGHT
    String type;
    Double fixedValue;
    WeightedTariffDTO[] weight;
    WeightIncrementDTO increment;
}
