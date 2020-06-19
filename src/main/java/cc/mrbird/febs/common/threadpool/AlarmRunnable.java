package cc.mrbird.febs.common.threadpool;

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
    @Autowired
    IOrderService orderService;

    @Autowired
    INoticeService noticeService;

    Long orderId;
    public AlarmRunnable(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        //修改订单状态，添加报警
        Order order = orderService.findOrderByOrderId(orderId);
        order.setIsAlarm("1");
        orderService.updateOrder(order);

        //websocket 通知前端
        WebSocketServer.sendInfo(3,"付款超时报警",  String.valueOf(FebsUtil.getCurrentUser().getUserId()));

        //添加通知
        Notice notice = new Notice();
        notice.setUserId(order.getApplyUserId());
        notice.setOrderId(order.getOrderId());
        notice.setDeviceId(order.getDeviceId());
        notice.setOrderNumber(order.getOrderNumber());
        notice.setAmount(order.getAmount());
        notice.setIsRead("0");
        notice.setContent("设备注资时间超过了30s");
        notice.setCreateTime(new Date());
        noticeService.save(notice);
        log.error("订单id为：" + orderId + "的订单超时了，给订单状态添加警告，通知前端");
    }
}
