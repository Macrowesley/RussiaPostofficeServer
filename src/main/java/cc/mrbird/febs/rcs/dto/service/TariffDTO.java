package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TariffDTO {
    //FIXED, WEIGHT
    String type;
    Double fixedValue;
    WeightedTariffDTO[] weigth;
    WeightIncrementDTO increment;
}
