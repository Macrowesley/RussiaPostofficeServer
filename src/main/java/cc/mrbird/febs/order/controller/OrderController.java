package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.order.entity.OrderExcel;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.utils.StatusUtils;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.select.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ControllerEndpoint(operation = "获取注资分页列表", exceptionMessage = "获取注资分页列表失败")
    @GetMapping("list")
    @RequiresPermissions("order:list")
    public FebsResponse orderList(QueryRequest request, OrderVo order) {
        log.info("请求参数：order={} , request={}", order.toString(), request.toString());
        IPage<OrderVo> pageInfo = this.orderService.findPageOrders(request, order);
        pageInfo.getRecords().stream().forEach(bean -> {
            bean.setBtnList(StatusUtils.getBtnMapList(bean.getOrderStatus()));
        });
        Map<String, Object> dataTable = getDataTable(pageInfo);
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "获取注资状态列表", exceptionMessage = "获取注资状态列表失败")
    @GetMapping("selectStatus")
    public FebsResponse selectStatus() {
        return new FebsResponse().success().data(StatusUtils.getOrderStatusList());
    }

    @ControllerEndpoint(operation = "获取表头号列表", exceptionMessage = "获取表头号列表失败")
    @GetMapping("getAcnumList")
    public FebsResponse getAcnumList() {
        List<Device> acnumList = deviceService.findDeviceListByUserId(FebsUtil.getCurrentUser().getUserId());
        return new FebsResponse().success().data(acnumList);
    }

    @ControllerEndpoint(operation = "获取审核员列表", exceptionMessage = "获取审核员列表失败")
    @GetMapping("getAuditUserNameList/{deviceId}")
    public FebsResponse getAuditUserNameList(@NotBlank @PathVariable String deviceId) {
        List<Map<String, Object>> userList = userService.findAuditListByDeviceId(deviceId);
        return new FebsResponse().success().data(userList);
    }

    @ControllerEndpoint(operation = "提交审核", exceptionMessage = "提交审核失败")
    @PostMapping("submitAuditApply/{orderId}")
    @RequiresPermissions("order:submitAuditApply")
    public FebsResponse submitAuditApply(@NotBlank @PathVariable String orderId) {
        this.orderService.submitAuditApply(Long.valueOf(orderId));
        return new FebsResponse().success();
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
    public FebsResponse editOrder(OrderVo order) {
        this.orderService.editOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "提交注资审核", exceptionMessage = "提交注资审核失败")
    @PostMapping("submitInjection")
    @RequiresPermissions("order:update")
    public FebsResponse submitInjectionOrder(OrderVo order) {
        log.info("提交注资审核 order = {}", order.toString());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "提交闭环审核", exceptionMessage = "提交闭环审核失败")
    @PostMapping("submitClose")
    @RequiresPermissions("order:update")
    public FebsResponse submitClose(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "显示审核详情", exceptionMessage = "显示审核详情失败")
    @PostMapping("auditDetail")
    @RequiresPermissions("order:update")
    public FebsResponse auditDetail(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "撤销", exceptionMessage = "撤销失败")
    @PostMapping("repeal")
    @RequiresPermissions("order:update")
    public FebsResponse repealOrder(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "冻结", exceptionMessage = "冻结失败")
    @PostMapping("freeze")
    @RequiresPermissions("order:update")
    public FebsResponse freezeOrder(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "解冻", exceptionMessage = "解冻失败")
    @PostMapping("unfreeze")
    @RequiresPermissions("order:update")
    public FebsResponse unfreezeOrder(OrderVo order) {
        //TODO
        return new FebsResponse().success();
    }

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
