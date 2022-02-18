package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CheckServiceResultDTO {
    //长度为2 1 成功
    String result;
    String version;
    //机器状态
    String fmStatus;
    //机器要改成的状态
    String futureStatus;
    //机器是否应该修改状态成未来状态 （1 需要改 0 不需要改）
    String isFmNeedChangeStatus;
    //1 俄罗斯改变状态  0 机器改变状态
    String isRussia;
    //订单是否结束（1 结束 0 未结束）
    String isPrintEnd;
    //机器的私钥是否需要更新（0 不需要更新 1需要更新）
    String isFmPrivateNeedUpdate;
    //机器的税率是否需要更新（0不需要 1需要更新）
    String isFmTaxNeedUpdate;
    //当前任务已经打印的总数量
    String actualCount;
    //当前任务已经打印的总金额 单位是分
    String actualAmount;
    //二维码内容（不包含签名）
    String dmMsg;
    String transactionId;
    String serverDate;
    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
    String printJobType;
    //ForeseenFMDTO的json
//    String foreseenFMDTO;

    //foreseen信息
    String foreseenId;
    String contractCode;
    String frankMachineId;
    String userId;
    String postOffice;
    Integer totalCount;
    ForeseenProductFmDto[] products;
    String taxVersion;
    //金钱单位：分
    String totalAmmount;
    String machineDate;
    /**
     * printJob的id
     */
    Integer printJobId;
}
