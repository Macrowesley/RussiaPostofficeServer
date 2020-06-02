package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.impl.DeviceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;

@Slf4j
@Controller("deviceView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "device")
public class ViewController {
    @Autowired
    DeviceServiceImpl deviceService;

    @GetMapping("device")
    @RequiresPermissions("device:view")
    public String deviceIndex(){
        return FebsUtil.view("device/device");
    }

    @GetMapping("add")
    @RequiresPermissions("device:add")
    public String deviceAdd() {
        return FebsUtil.view("device/deviceAdd");
    }

    @GetMapping("update/{deviceId}")
    @RequiresPermissions("device:update")
    public String deviceUpdate(@NotNull @PathVariable Long deviceId, Model model) {
        Device device = deviceService.findDeviceById(deviceId);
        device.setCreateTime(null);
        log.info("device信息 = " + device.toString());
        model.addAttribute("device", device);
        return FebsUtil.view("device/deviceUpdate");
    }

}
