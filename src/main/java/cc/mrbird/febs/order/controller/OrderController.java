package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
/**
 * 订单表 Controller
 *
 *
 * @date 2020-05-27 14:56:29
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class OrderController extends BaseController {

    private final IOrderService orderService;

    @GetMapping("order")
    @RequiresPermissions("order:list")
    public FebsResponse getAllOrders(Order order) {
        return new FebsResponse().success().data(orderService.findOrders(order));
    }

    @GetMapping("order/list")
    @RequiresPermissions("order:list")
    public FebsResponse orderList(QueryRequest request, Order order) {
        Map<String, Object> dataTable = getDataTable(this.orderService.findOrders(request, order));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("order")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid Order order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Order", exceptionMessage = "删除Order失败")
    @GetMapping("order/delete")
    @RequiresPermissions("order:delete")
    public FebsResponse deleteOrder(Order order) {
        this.orderService.deleteOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Order", exceptionMessage = "修改Order失败")
    @PostMapping("order/update")
    @RequiresPermissions("order:update")
    public FebsResponse updateOrder(Order order) {
        this.orderService.updateOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Order", exceptionMessage = "导出Excel失败")
    @PostMapping("order/excel")
    @RequiresPermissions("order:export")
    public void export(QueryRequest queryRequest, Order order, HttpServletResponse response) {
        List<Order> orders = this.orderService.findOrders(queryRequest, order).getRecords();
        ExcelKit.$Export(Order.class, response).downXlsx(orders, false);
    }
}
