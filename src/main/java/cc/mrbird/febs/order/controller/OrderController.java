package cc.mrbird.febs.order.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.AuditType;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.TokenUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.order.dto.AddOrderDTO;
import cc.mrbird.febs.order.dto.EditOrderDTO;
import cc.mrbird.febs.order.dto.EditOrderStatusDTO;
import cc.mrbird.febs.order.dto.SubmitApplyDTO;
import cc.mrbird.febs.order.entity.Order;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.text.NumberFormat;
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
@ApiIgnore
public class OrderController extends BaseController {
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private final IOrderService orderService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IUserService userService;

    @ControllerEndpoint(operation = "获取注资分页列表", exceptionMessage = "{order.operation.listError}")
    @GetMapping("list")
    @RequiresPermissions("order:list")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse orderList(QueryRequest request, OrderVo order) {
        log.info("请求订单列表");
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
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse selectStatus() {
        return new FebsResponse().success().data(StatusUtils.getOrderStatusList());
    }

    @ControllerEndpoint(operation = "获取表头号列表", exceptionMessage = "{order.operation.acnumListError}")
    @GetMapping("getAcnumList")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse getAcnumList() {
        List<Device> acnumList = deviceService.findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId());
        return new FebsResponse().success().data(acnumList);
    }

    @ControllerEndpoint(operation = "获取审核员列表", exceptionMessage = "{order.operation.auditListError}")
    @GetMapping("getAuditUserNameList/{deviceId}")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse getAuditUserNameList(@NotNull @PathVariable Long deviceId) {
        List<Map<String, Object>> userList = userService.findAuditListByDeviceId(deviceId);
        return new FebsResponse().success().data(userList);
    }

    @ControllerEndpoint(operation = "新增", exceptionMessage = "{order.operation.addError}")
    @PostMapping("add/{token}")
    @RequiresPermissions("order:add")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse addOrder(@Validated AddOrderDTO addOrderDTO, @PathVariable String token) {
        tokenUtil.validToken(token, FebsUtil.getCurrentUser().getUserId().toString());
        OrderVo order = new OrderVo();
        BeanUtils.copyProperties(addOrderDTO, order);
        this.orderService.createOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改", exceptionMessage = "{order.operation.editError}")
    @PostMapping("update")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse editOrder(@Validated EditOrderDTO editOrderDTO) {
        OrderVo order = new OrderVo();
        BeanUtils.copyProperties(editOrderDTO, order);
        this.orderService.editOrder(order);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "提交审核", exceptionMessage = "{order.operation.submitAuditError}")
    @PostMapping("submitApply")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse submitApply(@Validated SubmitApplyDTO submitApplyDTO) {
        String auditType = submitApplyDTO.getAuditType();
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(submitApplyDTO, orderVo);
        if (auditType.equals(AuditType.injection)){
            orderService.submitAuditApply(orderVo);
        }else if (auditType.equals(AuditType.closedCycle)){
            orderService.submitEndOrderApply(orderVo);
        }else{
            throw new FebsException("审核类型出错");
        }

        return new FebsResponse().success();
    }

    /*@ControllerEndpoint(operation = "显示审核详情", exceptionMessage = "{order.operation.auditDetailError}")
    @PostMapping("auditDetail")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse auditDetail(OrderVo order) {
        return new FebsResponse().success();
    }*/

    @ControllerEndpoint(operation = "注销", exceptionMessage = "{order.operation.repealError}")
    @PostMapping("cancel")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse cancelOrder(@Validated EditOrderStatusDTO orderStatusDTO) {
        orderService.cancelOrder(orderStatusDTO.getOrderId());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "冻结", exceptionMessage = "{order.operation.freezeError}")
    @PostMapping("freeze")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse freezeOrder(@Validated EditOrderStatusDTO orderStatusDTO) {
        orderService.freezeOrder(orderStatusDTO.getOrderId());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "解冻", exceptionMessage = "{order.operation.unfreezeError}")
    @PostMapping("unfreeze")
    @RequiresPermissions("order:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public FebsResponse unfreezeOrder(@Validated EditOrderStatusDTO orderStatusDTO) {
        orderService.unfreezeOrder(orderStatusDTO.getOrderId());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Order", exceptionMessage = "{order.operation.exportError}")
    @GetMapping("excel")
    @RequiresPermissions("order:export")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_order_order")
    public void export(QueryRequest queryRequest, OrderVo order, HttpServletResponse response) {
        List<OrderVo> orders = this.orderService.findAllOrders(queryRequest, order);
        orders.stream().forEach(orderVo -> {
            log.info(orderVo.toString());
        });
        ExcelKit.$Export(OrderExcel.class, response).downXlsx(orders, false);
    }
}
