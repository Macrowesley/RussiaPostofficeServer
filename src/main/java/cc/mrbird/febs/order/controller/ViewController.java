package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Validated
@Controller("orderView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "order")
public class ViewController {
    @Autowired
    IOrderService orderService;

    @GetMapping("order")
    @RequiresPermissions("order:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_order_view", isApi = false)
    public String orderIndex(Model model){
//        List<OrderBtnEnum> btnList = BtnPermissionUtils.getBtnList()
        return FebsUtil.view("order/order");
    }

    @GetMapping("add")
    @RequiresPermissions("order:add")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_view", isApi = false)
    public String orderAdd(){
        return FebsUtil.view("order/orderAdd");
    }

    @GetMapping("update/{orderId}")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_view", isApi = false)
    public String orderUpdate(@NotNull @Min(1) @PathVariable Long orderId, Model model){
        log.info("orderId = {}", orderId );
        //需要几个数据  device中的maxAmount acnum  审核人的id  还需要传递orderId进去
        Map<String, Object> order = orderService.findOrderDetailByOrderId(orderId);
        model.addAttribute("order", order);
        return FebsUtil.view("order/orderUpdate");
    }

    @ControllerEndpoint(operation = "提交注资审核", exceptionMessage = "{order.operation.submitAuditError}")
    @GetMapping("submitApply/{orderId}/{audityType}")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_view", isApi = false)
    public String submitApply(@NotNull @Min(1) @PathVariable Long orderId,
                                       @NotNull @Min(1) @Max(2) @PathVariable Integer audityType,
                                       Model model) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("audityType",audityType);
        return FebsUtil.view("order/orderSubmitInjection");
    }

}
