package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.OrderExcel;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.utils.StatusUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        IPage<OrderVo> pageInfo = this.orderService.findPageOrders(request, order);
        pageInfo.getRecords().stream().forEach(bean ->{
            bean.setBtnList(StatusUtils.getBtnMapList(bean.getOrderStatus()));
        });
        Map<String, Object> dataTable = getDataTable(pageInfo);
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("selectStatus")
    public FebsResponse selectStatus(){
        return new FebsResponse().success().data(StatusUtils.getOrderStatusList());
    }

    @ControllerEndpoint(operation = "提交审核", exceptionMessage = "提交审核失败")
    @PostMapping("submitAuditApply/{orderId}")
    @RequiresPermissions("order:submitAuditApply")
    public FebsResponse submitAuditApply(String orderId) {
        this.orderService.submitAuditApply(Long.valueOf(orderId));
        return new FebsResponse().success();
    }


/*    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Order", exceptionMessage = "新增Order失败")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }*/



    @ControllerEndpoint(operation = "导出Order", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("order:export")
    public void export(QueryRequest queryRequest, OrderVo order, HttpServletResponse response) {
        List<OrderVo> orders = this.orderService.findAllOrders(queryRequest, order);
        orders.stream().forEach(orderVo -> {
            log.info(orderVo.toString());
        });
        ExcelKit.$Export(OrderExcel.class, response).downXlsx(orders, false);
    }
}
