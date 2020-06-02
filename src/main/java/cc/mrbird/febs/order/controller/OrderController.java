package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private final IOrderService orderService;

    @GetMapping("list")
    @RequiresPermissions("order:list")
    public FebsResponse orderList(QueryRequest request, OrderVo order) {
        Map<String, Object> dataTable = getDataTable(this.orderService.findPageOrders(request, order));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }


    @ControllerEndpoint(operation = "修改Order", exceptionMessage = "修改Order失败")
    @PostMapping("update")
    @RequiresPermissions("order:update")
    public FebsResponse updateOrder(OrderVo order) {
        this.orderService.updateOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Order", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @RequiresPermissions("order:export")
    public void export(QueryRequest queryRequest, OrderVo order, HttpServletResponse response) {
        List<OrderVo> orders = this.orderService.findPageOrders(queryRequest, order).getRecords();
        ExcelKit.$Export(Order.class, response).downXlsx(orders, false);
    }
}
