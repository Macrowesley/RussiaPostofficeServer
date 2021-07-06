package cc.mrbird.febs.rcs.dto.manager;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"id","foreseenId","postOffice","frankMachineId","contractCode","contractId",
        "startDateTime","stopDateTime","userId","creditVal",
        "amount","count","graphId","taxVersion","franks"})
public class TransactionDTO {

    /**
     * format: uuid
     */
    String id;

    /**
     * format: uuid
     */
    String foreseenId;

    String postOffice;

    String frankMachineId;

    /**
     * 8位数字
     */
    String contractCode;
    String contractId;


    /**
     * format: 'date-time'
     */
    String startDateTime;

    /**
     * format: 'date-time'
     */
    String stopDateTime;

    String userId;

    Double creditVal;

    Double amount;

    int count;

    String graphId;

    /**
     * example: "A0042015A"
     */
    String taxVersion;

    FrankDTO[] franks;


}
