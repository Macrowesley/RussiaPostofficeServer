package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseensResultDTO {
    String contractId;
    String foreseenId;
    String consolidate;
    String current;
    AddressDTO[] addressList;
}
