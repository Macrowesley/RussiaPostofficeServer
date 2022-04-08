package cc.mrbird.febs.common.schedule;

import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
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
    ITransactionMsgService transactionMsgService;


    /**
     * 定时删除半年前的msg数据
     */
    @Scheduled(cron="0 0 2 * * ? ")
    @Transactional(propagation = Propagation.SUPPORTS)
    private void deleteTransactionMsgBySchedule(){
        log.info("定时删除半年前的msg数据");
        try {
            transactionMsgService.deleteTransactionMsgBySchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
