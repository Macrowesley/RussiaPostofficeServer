package cc.mrbird.febs;

import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.impl.DeviceServiceImpl;
import cc.mrbird.febs.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceServiceImplTest {
    @Autowired
    DeviceServiceImpl deviceService;

    @Autowired
    org.apache.shiro.mgt.SecurityManager securityManager;

    @BeforeEach
    public void setUp() {
        try {
            User user = FebsUtil.getCurrentUser();
        } catch (Exception e) {
            String username = "";
            String password = "";

            int type = 1;
            switch (type) {
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
    public void deleteDevicesByBindUserId() {
        deviceService.deleteDevicesByBindUserId(14L);
    }

    @Test
    public void selectSubUserDeviceListExcepBindUserIdByRoleAndParent() {
        long parentUserId = 12L;
        long bindUserId = 14L;
        long roleType = 4L;

        List<Device> allList = deviceService.findAllDeviceListByUserId(parentUserId);
        List<Device> bindList1 = deviceService.findAllDeviceListByUserId(14L);
        List<Device> bindList2 = deviceService.findAllDeviceListByUserId(15L);
        List<Device> bindList3 = deviceService.findAllDeviceListByUserId(18L);
        List<Integer> subUserList = Arrays.asList(14, 15, 18);

        log.info("父类开始循环");
        allList.stream().forEach(device -> {
            printDevice(device);
        });

        log.info("绑定的用户1循环");
        bindList1.stream().forEach(device -> {
            printDevice(device);
        });
        log.info("绑定的用户2循环");
        bindList2.stream().forEach(device -> {
            printDevice(device);
        });
        log.info("绑定的用户3循环");
        bindList3.stream().forEach(device -> {
            printDevice(device);
        });

        log.info("筛选后开始循环");//结果应该只有id = 5 6 7
        List<Device> list = deviceService.selectSubUserDeviceListExcepBindUserIdByRoleAndParent(bindUserId, parentUserId, roleType);
        list.stream().forEach(device -> {
            printDevice(device);
        });
    }

    @Test
    public void test() {
        long parentUserId = 12L;
        long bindUserId = 14L;
        long roleType = 4L;

        List<Device> parentUserDeviceList = deviceService.findAllDeviceListByUserId(parentUserId);
        List<Device> subUserDeviceList = deviceService.selectSubUserDeviceListExcepBindUserIdByRoleAndParent(bindUserId, parentUserId, roleType);


        Long[] res = deviceService.findDeviceIdArrByUserId(Long.valueOf(bindUserId));

        log.info("父类开始循环");
        parentUserDeviceList.stream().forEach(device -> {
            printDevice(device);
        });

        log.info("子类开始循环");
        subUserDeviceList.stream().forEach(device -> {
            printDevice(device);
        });

        log.info("过滤后的内容");
        //过滤后的deviceList
        List<Device> allFilterDeviceList = new ArrayList<>();
        subUserDeviceList.stream().forEach(device -> {
            parentUserDeviceList.remove(device);
        });
        parentUserDeviceList.stream().forEach(device -> {
            printDevice(device);
        });

        System.out.println(res);
    }

    public void printDevice(Device d) {
        log.info(" id = {}, acnum = {}", d.getDeviceId(), d.getAcnum());
    }
}