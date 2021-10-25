package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"product","tarif","modified"})
public class RateDetailDTO {
    PostalProductDTO product;
    TariffDTO tarif;
    String modified;
}
