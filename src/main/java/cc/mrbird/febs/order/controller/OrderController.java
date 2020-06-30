package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.AuditType;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.order.entity.OrderExcel;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.utils.StatusUtils;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 订单表 Controller
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

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IUserService userService;

    @ControllerEndpoint(operation = "获取注资分页列表", exceptionMessage = "{order.operation.listError}")
    @GetMapping("list")
    @RequiresPermissions("order:list")
    public FebsResponse orderList(QueryRequest request, OrderVo order) {
//        log.info("请求参数：order={} , request={}", order.toString(), request.toString());
        IPage<OrderVo> pageInfo = this.orderService.findPageOrders(request, order);
        pageInfo.getRecords().stream().forEach(bean -> {
            bean.setBtnList(StatusUtils.getOrderBtnMapList(bean.getOrderStatus()));
        });
        Map<String, Object> dataTable = getDataTable(pageInfo);
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "获取注资状态列表", exceptionMessage = "{order.operation.listError}")
    @GetMapping("selectStatus")
    public FebsResponse selectStatus() {
        return new FebsResponse().success().data(StatusUtils.getOrderStatusList());
    }

    @ControllerEndpoint(operation = "获取表头号列表", exceptionMessage = "{order.operation.acnumListError}")
    @GetMapping("getAcnumList")
    public FebsResponse getAcnumList() {
        List<Device> acnumList = deviceService.findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId());
        return new FebsResponse().success().data(acnumList);
    }

    @ControllerEndpoint(operation = "获取审核员列表", exceptionMessage = "{order.operation.auditListError}")
    @GetMapping("getAuditUserNameList/{deviceId}")
    public FebsResponse getAuditUserNameList(@NotBlank @PathVariable String deviceId) {
        List<Map<String, Object>> userList = userService.findAuditListByDeviceId(deviceId);
        return new FebsResponse().success().data(userList);
    }

    @ControllerEndpoint(operation = "新增", exceptionMessage = "{order.operation.addError}")
    @PostMapping("add")
    @RequiresPermissions("order:add")
    public FebsResponse addOrder(@Valid OrderVo order) {
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改", exceptionMessage = "{order.operation.editError}")
    @PostMapping("update")
    @RequiresPermissions("order:update")
    public FebsResponse editOrder(OrderVo order) {
        this.orderService.editOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "提交审核", exceptionMessage = "{order.operation.submitAuditError}")
    @PostMapping("submitApply/{auditType}")
    @RequiresPermissions("order:update")
    public FebsResponse submitApply(OrderVo orderVo, @NotBlank @PathVariable String auditType) {
        if (auditType.equals(AuditType.injection)){
            orderService.submitAuditApply(orderVo);
        }else if (auditType.equals(AuditType.closedCycle)){
            orderService.submitEndOrderApply(orderVo);
        }else{
            throw new FebsException("审核类型出错");
        }

        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "显示审核详情", exceptionMessage = "{order.operation.auditDetailError}")
    @PostMapping("auditDetail")
    @RequiresPermissions("order:update")
    public FebsResponse auditDetail(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "注销", exceptionMessage = "{order.operation.repealError}")
    @PostMapping("cancel/{orderId}")
    @RequiresPermissions("order:update")
    public FebsResponse cancelOrder(@NotBlank @PathVariable String orderId) {
        orderService.cancelOrder(Long.valueOf(orderId));
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "冻结", exceptionMessage = "{order.operation.freezeError}")
    @PostMapping("freeze/{orderId}")
    @RequiresPermissions("order:update")
    public FebsResponse freezeOrder(@NotBlank @PathVariable String orderId) {
        orderService.freezeOrder(Long.valueOf(orderId));
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "解冻", exceptionMessage = "{order.operation.unfreezeError}")
    @PostMapping("unfreeze/{orderId}")
    @RequiresPermissions("order:update")
    public FebsResponse unfreezeOrder(@NotBlank @PathVariable String orderId) {
        orderService.unfreezeOrder(Long.valueOf(orderId));
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Order", exceptionMessage = "{order.operation.exportError}")
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
