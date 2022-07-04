package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmReqDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseenFmReqDTO {
    String id;
    String contractCode;
    String frankMachineId;
    String userId;
    String postOffice;
    Integer totalCount;
    ForeseenProductFmReqDTO[] products;
    String taxVersion;
    //金钱单位：分
    String totalAmmount;
    String machineDate;
    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
    Integer printJobType;
    /**
     * printJob的id
     */
    Integer printJobId;
}
