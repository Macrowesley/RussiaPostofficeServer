package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TariffDTO {
    String type;
    WeigthDTO[] weigth;
    IncrementDTO increment;
    Double fixedValue;
}
