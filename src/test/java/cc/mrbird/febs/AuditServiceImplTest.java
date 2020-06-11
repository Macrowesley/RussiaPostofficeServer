package cc.mrbird.febs;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.order.entity.OrderVo;
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

import java.util.List;

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
                    //审核管理员
                    username = "shebei";
                    password = "111111";
                    break;
                case 4:
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
    }

    @Test
    void findPageAudits() {
        QueryRequest request = new QueryRequest();
        request.setPageNum(1);
        request.setPageSize(10);

        Audit audit = new Audit();
        List<Audit> list =  auditService.findPageAudits(request,audit).getRecords();
        for (Audit bean : list) {
            log.info(bean.toString());
        }
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