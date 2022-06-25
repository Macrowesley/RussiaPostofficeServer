package cc.mrbird.febs.common.netty.protocol.dto;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmReqDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmRespDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ForeseensFmRespDTO {
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
    /**
     * PC订单 打印对象类型：过戳还是签条
     * 1 stamp
     * 2 stick
     */
    private int printObjectType;
    //pc创建订单时，给机器的，再带过来
    Integer printJobId;
    ForeseenProductFmRespDTO[] products;
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
