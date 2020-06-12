package cc.mrbird.febs;

import cc.mrbird.febs.common.entity.AuditType;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.IdUtil;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.service.impl.OrderServiceImpl;
import cc.mrbird.febs.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class OrderServiceImplTest {
    @Autowired
    org.apache.shiro.mgt.SecurityManager securityManager;

    @Autowired
    IOrderService orderService;

    @Autowired
    IUserService userService;

    @BeforeEach
    public void login(){
        log.error("开始登陆");
        String username = "";
        String password = "";

        int type = 1;
        switch (type){
            case 1:
                //系统管理员
                username = "admin";
                password = "111111";
                break;
            case 2:
                //机构管理员
                username = "jigou";
                password = "111111";
                break;
            case 3:
                //设备管理员
                username = "shebei";
                password = "111111";
                break;
        }

        SecurityUtils.setSecurityManager(securityManager);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        final Subject subject = SecurityUtils.getSubject();

        subject.login(token);
    }

    @Test
    public void findPageOrders() {
        QueryRequest request = new QueryRequest();
        request.setPageNum(1);
        request.setPageSize(10);

        OrderVo orderVo = new OrderVo();
        List<OrderVo> list =  orderService.findPageOrders(request,orderVo).getRecords();
        for (OrderVo vo : list) {
            log.info(vo.toString());
        }
    }

    @Test
    void createOrder() {
    }

    @Test
    void submitAuditApply() {
    }

    @Test
    void createOrderAndSubmitApply() {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderStatus(OrderStatusEnum.createOrder.getStatus());
        orderVo.setAmount("90000");

        orderVo.setDeviceId(5L);
        orderVo.setExpireDays(7);
        orderVo.setAuditUserId(15L);
        orderVo.setEndTime(DateUtil.getDateAfter(new Date(), orderVo.getExpireDays()));


        orderVo.setAcnum("CPU111");
        orderVo.setOrderNumber(IdUtil.cureateId());
        orderVo.setAuditType(AuditType.injection);
        orderService.createOrderAndSubmitApply(orderVo);
    }

    @Test
    void machineRequestData() {

    }

    @Test
    void updateMachineInjectionStatus() {
    }

    @Test
    void submitEndOrderApply() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void freezeOrder() {
    }

    @Test
    void findOrderByOrderId() {
       Order order = orderService.findOrderByOrderId(8L);
       log.info(order.toString());
    }

    @Test
    void updateOrder() {
        Order order = new Order();
        order.setOrderId(8L);
        order.setOrderStatus(OrderStatusEnum.auditIng.getStatus());
        orderService.updateOrder(order);
    }

    @Test
    void findAuditListByDeviceId(){
        String deviceId = "1";
        userService.findAuditListByDeviceId(deviceId).stream().forEach(user -> log.info(user.toString()));
    }

    @Test
    void selectAllExpireOrderAndUpdateAndNoticeUser(){
        orderService.selectAllExpireOrderAndUpdateAndNoticeUser();
    }
}