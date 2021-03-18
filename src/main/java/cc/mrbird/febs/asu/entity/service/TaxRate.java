package cc.mrbird.febs.asu.entity.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaxRate {
    /**
     * 重量，g（上限）
     */
    Integer weight;

    /**
     * 不含增值税的关税（RUB）
     */
    Double noVAT;

    /**
     * 含增值税关税（RUB）
     */
    Double withVAT;

    /**
     * 增值税金额（卢布）
     */
    Double sumVAT;

    /**
     * 增值税率
     */
    Double rateVAT;
}
