package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
@Controller("orderView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "order")
public class ViewController {
    @Autowired
    IOrderService orderService;

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
    public String orderUpdate(@NotBlank @PathVariable String orderId, Model model){
        log.info("orderId = {}", orderId );
        //需要几个数据  device中的maxAmount acnum  审核人的id  还需要传递orderId进去
        Map<String, Object> order = orderService.findOrderDetailByOrderId(orderId);
        model.addAttribute("order", order);
        return FebsUtil.view("order/orderUpdate");
    }

    @ControllerEndpoint(operation = "提交注资审核", exceptionMessage = "{order.operation.submitAuditError}")
    @GetMapping("submitApply/{orderId}/{audityType}")
    @RequiresPermissions("order:update")
    public String submitApply(@NotBlank @PathVariable String orderId,
                                       @NotBlank @PathVariable String audityType,
                                       Model model) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("audityType",audityType);
        return FebsUtil.view("order/orderSubmitInjection");
    }

}
