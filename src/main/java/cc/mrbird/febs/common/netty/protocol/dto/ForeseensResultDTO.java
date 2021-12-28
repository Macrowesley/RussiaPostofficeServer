package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseensResultDTO {
    String contractCode;
    String foreseenId;
    String consolidate;
    String current;
    String serverDate;
    AddressDTO[] addressList;
    /**
     * 1 机器创建的订单  2 PC创建的订单
     */
    Integer printJobType;
    //pc创建订单时，给机器的，再带过来
    Integer printJobId;
    ForeseenProductFmDto[] products;
    String totalCount;
    //单位分
    String totalAmount;
    //当前任务已经打印的总数量
    int actualCount = 0;
    //当前任务已经打印的总金额 单位是分
    String actualAmount = "0";
    //是否有transaction信息 0 没有 1 有
    String hasTranaction;
    //网络订单，服务器给机器临时保存的pc用户ID，机器返回结果时，带上这个ID
    String pcUserId;
}
