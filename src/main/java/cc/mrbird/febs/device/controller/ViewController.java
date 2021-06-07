package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.impl.DeviceServiceImpl;
import cc.mrbird.febs.device.vo.UserDeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@Controller("deviceView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "device")
@ApiIgnore
public class ViewController {
    @Autowired
    DeviceServiceImpl deviceService;


    @GetMapping("device")
    @RequiresPermissions("device:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_view", isApi = false)
    public String deviceIndex(){
        return FebsUtil.view("device/device");
    }

    @GetMapping("add")
    @RequiresPermissions("device:add")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_view", isApi = false)
    public String deviceAdd() {
        return FebsUtil.view("device/deviceAdd");
    }

    @GetMapping("update/{deviceId}")
    @RequiresPermissions("device:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_view", isApi = false)
    public String deviceUpdate(@NotNull @Min(1) @PathVariable Long deviceId, Model model) {
        Device device = deviceService.findDeviceById(deviceId);
        device.setCreatedTime(null);
        log.info("device信息 = " + device.toString());
        model.addAttribute("device", device);

        //查询这个设备绑定了哪个机构管理员
        if (FebsUtil.getCurrentUser().getRoleId().equals(RoleType.systemManager)) {
            UserDeviceVO userDeviceVO = deviceService.findByDeviceIdAndRoleId(deviceId, Long.valueOf(RoleType.organizationManager));
            model.addAttribute("userDevice", userDeviceVO);
        }
        return FebsUtil.view("device/deviceUpdate");
    }

    @GetMapping("detail/{deviceId}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_view", isApi = false)
    public String detail(@PathVariable Long deviceId, Model model) {
        log.info("设备详情");
        Device device = deviceService.findDeviceById(deviceId);
        model.addAttribute("device",device);
        return FebsUtil.view("device/deviceDetail");
    }

}
