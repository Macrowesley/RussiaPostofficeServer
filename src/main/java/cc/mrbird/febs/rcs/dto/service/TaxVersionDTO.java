package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaxVersionDTO {
    String version;

    /**
     * format: 'date-time'
     */
    String createDate;

    /**
     * format: 'date-time'
     */
    String applyDate;

    /**
     * format: 'date-time'
     */
    String publishDate;

    /**
     * format: 'date-time'
     */
    String changeDate;

    String description;

    PostalProductDTO[] products;
}
