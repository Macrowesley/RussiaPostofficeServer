package cc.mrbird.febs.rcs.common.context.order.status;

import cc.mrbird.febs.rcs.common.context.order.status.entity.Result;
import cc.mrbird.febs.rcs.common.context.order.status.enums.RcsOrderStatusEnum;
import cc.mrbird.febs.rcs.common.context.order.status.service.IRcsOrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 管理状态变化
 * @author Administrator
 */
@Slf4j
@Service
public class OrderStatusContext {
    @Autowired
    private final Map<String, IRcsOrderStatusService> map = null;
//    private final ConcurrentHashMap<String, IRcsOrderStatusService> map = new ConcurrentHashMap<>();

    public IRcsOrderStatusService getRescource(RcsOrderStatusEnum currentStatus){
        return map.get(currentStatus.getStatus());
    }

    /**
     * @param currentStatus
     */
    public Result doBegin(String orderId, RcsOrderStatusEnum currentStatus) {
        //todo 测试 打印map信息，看是否自动装配
        log.info("打印map信息，看是否自动装配 map中数量" + map.entrySet().size());
        map.entrySet().forEach(item->{
            log.info("key={}, value={}", item.getKey(), item.getValue());
        });
        IRcsOrderStatusService iRcsOrderStatusService = map.get(currentStatus.getStatus());
        log.info("得到的iRcsOrderStatusService ： " + iRcsOrderStatusService.getClass().getName());


        getRescource(currentStatus).doBegin(orderId, currentStatus);
        return null;
    }

    /**
     * @param currentStatus
     */
    public Result doForeseen(RcsOrderStatusEnum currentStatus) {
        return null;
    }

    /**
     * @param currentStatus
     */
    public Result doTransaction(RcsOrderStatusEnum currentStatus) {
        return null;
    }

}
