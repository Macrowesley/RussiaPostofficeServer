package cc.mrbird.febs.rcs.dto.ui;

import lombok.Data;
import lombok.ToString;

/**
 * @author Administrator
 */
@Data
@ToString
public class PrintJobProductDto {
    String code;
    int count;
    double weight;
    String address;
}
