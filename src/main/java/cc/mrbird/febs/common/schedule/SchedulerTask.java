package cc.mrbird.febs.common.schedule;

import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SchedulerTask {
    @Autowired
    IOrderService orderService;

    /**
     * 定时检查订单是否过期
     */
    @Scheduled(cron="0 0 0/1 * * ?")
    @Transactional(propagation = Propagation.SUPPORTS)
    private void checkOrderIsExpire(){
//        log.info("每隔一小时 —— 检查订单是否过期");
//        orderService.selectAllExpireOrderAndUpdateAndNoticeUser();
    }
}
