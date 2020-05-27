package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("orderView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "order")
public class ViewController {

    @GetMapping("order")
    @RequiresPermissions("order:view")
    public String orderIndex(){
        return FebsUtil.view("order/order");
    }

}
