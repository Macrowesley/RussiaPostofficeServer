package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeightedTariffDTO {
    int from;
    int to;
    double value;
}
