package cc.mrbird.febs.asu.entity.manager;

import cc.mrbird.febs.asu.entity.enums.Event;
import cc.mrbird.febs.asu.entity.enums.FMStatus;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class FrankMachine {
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

    FMStatus status;

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
    Event event;

    FMError error;
}
