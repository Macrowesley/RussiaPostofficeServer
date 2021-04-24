package cc.mrbird.febs.rcs.dto.manager;

import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class DeviceDTO {
    /*
    {
      "id": "FM100001",
      "timeStamp": "2021-03-18",
      "status": "ENABLED",
      "postOffice": "131000",
      "taxVersion": "A0042015A",
      "event": "STATUS",
      "error": {
        "code": "200",
        "message": "ok"
      }
    }
     */
    /**
     * FM编号
     * FM100001
     */
    String id;

    /**
     * date-time
     * 2021-01-01T09:00:00.001+03:00
     */
    String dateTime;

    FMStatusEnum status;

    /**
     * 131000
     */
    @Length(min = 6, max = 6)
    String postOffice;

    /**
     * A0042015A
     */
    String taxVersion;

    /**
     * type: string
     * enum:
     * - STATUS
     * - RATE_TABLE_UPDATE
     * example:
     * 'STATUS'
     */
    EventEnum eventEnum;

    FMError error;
}
