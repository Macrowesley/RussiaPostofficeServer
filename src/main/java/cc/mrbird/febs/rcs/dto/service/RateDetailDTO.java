package cc.mrbird.febs.rcs.dto.service;

import cc.mrbird.febs.rcs.entity.Tariff;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RateDetailDTO {
    PostalProductDTO product;
    TariffDTO tarif;
    String modified;
}
