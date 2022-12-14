package cc.mrbird.febs.rcs.dto.manager;

import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class DeviceTest {
    /*
    {
    "id":"NE100700",
    "postOffice":"394040",
    "status":"ENABLED",
    "taxVersion":"1.0",
    "dateTime":"2021-05-14T09:00:00.001+03:00"
}

{
    "id":"NE100700",
    "postOffice":"394040",
    "status":"ENABLED",
    "taxVersion":"1.0",
    "dateTime":"2021-05-14T09:00:00.001+03:00"
}
     */
    /**
     * FM编号
     * FM100001
     * frankmachineId
     */
    String id;

    /**
     * date-time
     * 2021-01-01T09:00:00.001+03:00
     */
    String dateTime;


    String status;

    /**
     * 131000
     */
    @Length(min = 6, max = 6)
    String postOffice;

    /**
     * A0042015A
     */
    String taxVersion;

}
