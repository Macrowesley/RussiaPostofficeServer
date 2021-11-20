package cc.mrbird.febs.rcs.common.context.status.service.impl;

import cc.mrbird.febs.job.service.IJobService;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.rcs.common.context.status.entity.Result;
import cc.mrbird.febs.rcs.common.context.status.enums.RcsOrderStatusEnum;
import cc.mrbird.febs.rcs.common.context.status.service.IRcsOrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component("begin")
public class BeginStatusImpl implements IRcsOrderStatusService {
    @Autowired
    IJobService jobService;

    /**
     * @param currentStatus
     */
    @Override
    public Result doBegin(String orderId, RcsOrderStatusEnum currentStatus) {
        log.info("创建订单后，当前状态是{}，然后要做这些事情", currentStatus.getStatus());
//        jobService.changeStatus(orderId, currentStatus, RcsOrderStatusEnum.Begin);
        return null;
    }

    /**
     * @param currentStatus
     */
    @Override
    public Result doForeseen(RcsOrderStatusEnum currentStatus) {
        return null;
    }

    /**
     * @param currentStatus
     */
    @Override
    public Result doTransaction(RcsOrderStatusEnum currentStatus) {
        return null;
    }
}
