package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaxVersionDTO {
    String version;

    String description;

    String source;

    RateDetailDTO[] details;

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
    String modified;

    /**
     * format: 'date-time'
     */
//    String createDate;


}
