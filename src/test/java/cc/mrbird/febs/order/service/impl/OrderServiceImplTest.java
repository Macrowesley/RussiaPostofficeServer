package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.common.entity.AuditType;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.IdUtil;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.order.entity.OrderVo;
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
    OrderServiceImpl orderService;

    @BeforeEach
    public void login(){
        log.error("开始登陆");
        String username = "shebei";
        String password = "123456";

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
    void submitAuditApply() {
        //        long curUserId = 14;
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderStatus(OrderStatusEnum.beginApply.getStatus());
        orderVo.setAmount("90000");

        orderVo.setDeviceId(5L);
        orderVo.setExpireDays(7);
        orderVo.setEndTime(DateUtil.getDateAfter(new Date(), orderVo.getExpireDays()));


        orderVo.setAcnum("CPU111");
        orderVo.setOrderNumber(IdUtil.cureateId());
        orderVo.setAuditType(AuditType.injection);
//        orderService.submitAuditApply(orderVo);
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
    void findOrderByMachine() {
    }

    @Test
    void updateOrder() {
    }
}