package cc.mrbird.febs.device.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DeviceServiceImplTest {
    @Autowired
    DeviceServiceImpl deviceService;

    @Test
    void findDevices() {
    }

    @Test
    void findDeviceListByUserId() {
    }

    @Test
    void findDeviceIdArrByUserId() {
    }

    @Test
    void createDevice() {
    }

    @Test
    void updateDevice() {
    }

    @Test
    void deleteDevice() {
    }

    @Test
    void getRepetitionInfo() {
    }

    @Test
    void saveDeviceList() {
    }

    @Test
    void findDeviceById() {
    }

    @Test
    void bindDevicesToUser() {
    }

    @Test
    void deleteDevicesByBindUserId() {
        deviceService.deleteDevicesByBindUserId(14L);
    }
}