package cc.mrbird.febs.audit.service.impl;

import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.system.entity.User;
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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class AuditServiceImplTest {
    @Autowired
    org.apache.shiro.mgt.SecurityManager securityManager;

    @Autowired
    IAuditService auditService;

    @BeforeEach
    void setUp() {
        try {
            User user = FebsUtil.getCurrentUser();
        }catch (Exception e){
            log.error("开始登陆");
            String username = "shebei";
            String password = "123456";

            SecurityUtils.setSecurityManager(securityManager);
            password = Md5Util.encrypt(username.toLowerCase(), password);
            final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            final Subject subject = SecurityUtils.getSubject();

            subject.login(token);
        }
    }

    @Test
    void findPageAudits() {
    }

    @Test
    void findAuditList() {
    }

    @Test
    void createAudit() {
    }

    @Test
    void updateAudit() {
    }

    @Test
    void deleteAudit() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void getNewestOneByOrderId() {
        log.info(auditService.getNewestOneByOrderId(8L).toString());
    }
}