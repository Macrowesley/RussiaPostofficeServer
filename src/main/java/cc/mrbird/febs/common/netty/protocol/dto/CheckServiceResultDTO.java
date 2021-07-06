package cc.mrbird.febs.common.netty.protocol.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CheckServiceResultDTO {
    //长度为2 1 成功
    String result;
    String version;
    //机器状态
    int fmStatus;
    //订单是否结束（1 结束 0 未结束）
    int isPrintEnd;
    //机器的私钥是否需要更新（0 不需要更新 1需要更新）
    int isFmPrivateNeedUpdate;
    //机器的税率是否需要更新（0不需要 1需要更新）
    int isFmTaxNeedUpdate;
    //当前任务已经打印的总数量
    int actualCount;
    //当前任务已经打印的总金额 单位是分
    String actualAmount;
    //二维码内容（不包含签名）
    String dmMsg;
    String transactionId;
    //ForeseenFMDTO的json
    String foreseenFMDTO;
}
