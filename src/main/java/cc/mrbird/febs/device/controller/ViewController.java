package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("deviceView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "device")
public class ViewController {

    @GetMapping("device")
    @RequiresPermissions("device:view")
    public String deviceIndex(){
        return FebsUtil.view("device/device");
    }

}
