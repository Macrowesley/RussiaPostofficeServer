package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TransactionFMDTO {
    String id;
    String foreseenId;
    String postOffice;
    String frankMachineId;
    String contractCode;
    /**
     * 花费时间，单位；分
     */
    String costTime;
    String userId;
    //金钱单位：分
    String creditVal;
    //金钱单位：分
    String amount;
    int count;
    String graphId;
    String taxVersion;
    //这个是服务器构建的内容，机器不用发送
    FrankDTO[] franks;
    //具体什么数字代表什么原因，以后扩展，目前默认为1，理由：得想一个
    int cancelMsgCode;
}
