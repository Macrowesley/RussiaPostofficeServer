package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"PostalProductDTO","TariffDTO","String"})
public class RateDetailDTO {
    PostalProductDTO product;
    TariffDTO tarif;
    String modified;
}
