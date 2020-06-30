package cc.mrbird.febs.common.threadpool;

import cc.mrbird.febs.common.configure.BeanContext;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.websocket.WebSocketServer;
import cc.mrbird.febs.notice.entity.Notice;
import cc.mrbird.febs.notice.service.INoticeService;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class AlarmRunnable implements Runnable {

    Long orderId;
    public AlarmRunnable(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        log.info("运行警报");

        IOrderService orderService = BeanContext.getBean(IOrderService.class);

        INoticeService noticeService = BeanContext.getBean(INoticeService.class);

        //修改订单状态，添加报警
        Order order = orderService.findOrderByOrderId(orderId);
        order.setIsAlarm("1");
        orderService.updateOrder(order);

        //添加通知
        Notice notice = new Notice();
        notice.setUserId(order.getApplyUserId());
        notice.setOrderId(order.getOrderId());
        notice.setDeviceId(order.getDeviceId());
        notice.setOrderNumber(order.getOrderNumber());
        notice.setAmount(order.getAmount());
        notice.setIsRead("0");
        notice.setContent(MessageUtils.getMessage("notice.alarmMessage"));
        notice.setCreateTime(new Date());
        noticeService.save(notice);

        //websocket 通知前端
        WebSocketServer.sendInfo(3, MessageUtils.getMessage("alarm.overtime"),  String.valueOf(order.getApplyUserId()));

        log.error("订单id为：" + orderId + "的订单超时了，给订单状态添加警告，通知前端");
    }
}
