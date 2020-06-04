package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

@Controller("orderView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "order")
public class ViewController {

    @GetMapping("order")
    @RequiresPermissions("order:view")
    public String orderIndex(Model model){
//        List<OrderBtnEnum> btnList = BtnPermissionUtils.getBtnList()
        return FebsUtil.view("order/order");
    }

    @GetMapping("add")
    @RequiresPermissions("order:add")
    public String orderAdd(){
        return FebsUtil.view("order/orderAdd");
    }

    @GetMapping("update/{orderId}")
    @RequiresPermissions("order:update")
    public String orderUpdate(@NotBlank String orderId){
        return FebsUtil.view("order/orderUpdate");
    }

}
