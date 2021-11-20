package cc.mrbird.febs.rcs.common.context.status.service;

import cc.mrbird.febs.rcs.common.context.status.entity.Result;
import cc.mrbird.febs.rcs.common.context.status.enums.RcsOrderStatusEnum;

/**
 * RCS 订单不同状态情况下的操作
 * @author Administrator
 */
public interface IRcsOrderStatusService {

    /**
     *
     */
    abstract Result doBegin(String orderId, RcsOrderStatusEnum currentStatus);

    /**
     *
     */
    abstract Result doForeseen(RcsOrderStatusEnum currentStatus);

    /**
     *
     */
    abstract Result doTransaction(RcsOrderStatusEnum currentStatus);
}
