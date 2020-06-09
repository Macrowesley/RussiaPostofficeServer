package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.audit.service.impl.AuditServiceImpl;
import cc.mrbird.febs.common.entity.*;

import cc.mrbird.febs.common.enums.OrderBtnEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.mapper.OrderMapper;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.utils.StatusUtils;
import cc.mrbird.febs.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表 Service实现
 *
 * @date 2020-05-27 14:56:29
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final OrderMapper orderMapper;
    private final IAuditService auditService;
    private final IDeviceService deviceService;

    @Override
    public IPage<OrderVo> findPageOrders(QueryRequest request, OrderVo orderVo) {

        Page<OrderVo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "order_id", FebsConstant.ORDER_DESC, false);

        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();
        long curUserId = curUser.getUserId();

        switch (roleId) {
            case RoleType.systemManager:
                return this.orderMapper.selectBySystemManager(page, orderVo);
            case RoleType.organizationManager:
                //根据设备id获取order列表
                return this.orderMapper.selectByOrganizationManager(page, curUserId, orderVo);
            default:
                //根据userid获取order列表
                log.info(orderVo.toString());
                return this.orderMapper.selectByUserId(page, curUserId, orderVo);
        }
    }

    @Override
    public List<OrderVo> findAllOrders(QueryRequest request, OrderVo orderVo) {

        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();
        long curUserId = curUser.getUserId();

        switch (roleId) {
            case RoleType.systemManager:
                return this.orderMapper.selectBySystemManager(orderVo);
            case RoleType.organizationManager:
                //根据设备id获取order列表
                return this.orderMapper.selectByOrganizationManager(curUserId, orderVo);
            default:
                //根据userid获取order列表
                log.info(orderVo.toString());
                return this.orderMapper.selectByUserId(curUserId, orderVo);
        }
    }

    /**
     * 单纯创建一个订单
     *
     * @param orderVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createOrder(OrderVo orderVo) {
        //验证金额
        checkAmountIsOk(orderVo);

        orderVo.setOrderStatus(OrderStatusEnum.createOrder.getStatus());
        addOtherParams(orderVo);
        this.baseMapper.insert(orderVo);
        return orderVo.getOrderId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editOrder(OrderVo orderVo) {
        //验证金额
        Order order = findOrderByOrderId(orderVo.getOrderId());
        orderVo.setDeviceId(order.getDeviceId());

        StatusUtils.checkBtnPermissioin(OrderBtnEnum.submitInjectionBtn, order.getOrderStatus());
        checkAmountIsOk(orderVo);


        //修改截止日期 、 截止天数、 金额
        order.setEndTime(DateUtil.getDateAfter(order.getCreateTime(), orderVo.getExpireDays()));
        order.setExpireDays(orderVo.getExpireDays());
        order.setAmount(orderVo.getAmount());
        updateOrder(order);
    }

    /**
     * 验证金额
     *
     * @param orderVo
     */
    private void checkAmountIsOk(OrderVo orderVo) {
        Device device = deviceService.findDeviceById(orderVo.getDeviceId());
        if (device == null) {
            throw new FebsException("没有该设备");
        }
        String maxAmount = device.getMaxAmount();
        if (Float.valueOf(orderVo.getAmount()) > Float.valueOf(maxAmount)) {
            throw new FebsException("金额超过" + maxAmount + "，无法注资");
        }
    }

    /**
     * 添加其他默认参数
     *
     * @param orderVo
     */
    private void addOtherParams(OrderVo orderVo) {
        orderVo.setApplyUserId(FebsUtil.getCurrentUser().getUserId());
        orderVo.setIsExpire("0");
        orderVo.setOrderNumber(IdUtil.cureateId());
        orderVo.setAmount(MoneyUtils.moneySaveTwoDecimal(orderVo.getAmount()));
        orderVo.setCreateTime(new Date());
        orderVo.setEndTime(DateUtil.getDateAfter(new Date(), orderVo.getExpireDays()));
    }

    /**
     * 提交审核
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAuditApply(OrderVo orderVo) {
        Order order = findOrderByOrderId(orderVo.getOrderId());
        StatusUtils.checkBtnPermissioin(OrderBtnEnum.submitInjectionBtn, order.getOrderStatus());

        //修改订单状态
        order.setOrderStatus(OrderStatusEnum.auditIng.getStatus());
        updateOrder(order);

        //添加注资申请
        order.setSubmitInfo(orderVo.getSubmitInfo());
        auditService.createAudit(order, AuditType.injection);
    }

    /**
     * 添加订单的时候就直接发起申请
     *
     * @param orderVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrderAndSubmitApply(OrderVo orderVo) {

        orderVo.setOrderStatus(OrderStatusEnum.auditIng.getStatus());
        addOtherParams(orderVo);
        this.baseMapper.insert(orderVo);

        //添加注资申请
        auditService.createAudit(orderVo, AuditType.injection);
    }

    /**
     * 机器获取数据包
     *
     * @param orderId
     * @return
     */
    @Override
    public Order machineRequestData(Long orderId) {
        Order order = findOrderByOrderId(orderId);
        //修改订单状态
        order.setOrderStatus(OrderStatusEnum.machineGetData.getStatus());
        updateOrder(order);
        return order;
    }

    /**
     * 更新机器注资状态
     *
     * @param orderVo
     * @param injectionStatus
     */
    @Override
    public void updateMachineInjectionStatus(OrderVo orderVo, boolean injectionStatus) {
        Order order = findOrderByOrderId(orderVo.getOrderId());
        if (injectionStatus) {
            order.setOrderStatus(OrderStatusEnum.machineInjectionSuccess.getStatus());
        } else {
            order.setOrderStatus(OrderStatusEnum.machineInjectionFail.getStatus());
        }
        updateOrder(order);
    }


    /**
     * 提交闭环订单申请
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitEndOrderApply(Long orderId) {
        Order orderVo = findOrderByOrderId(orderId);
        //修改订单状态
        orderVo.setOrderStatus(OrderStatusEnum.orderCloseApplyIng.getStatus());
        updateOrder(orderVo);

        //添加闭环申请
        auditService.createAudit(orderVo, AuditType.closedCycle);
    }

    /**
     * 撤销注资
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        //修改订单状态
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orderId);
        orderVo.setOrderStatus(OrderStatusEnum.orderRepeal.getStatus());
        updateOrder(orderVo);

        //修改审核订单状态
        auditService.cancelOrder(orderId);
    }

    /**
     * 冻结注资
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeOrder(Long orderId) {
        Order order = this.baseMapper.selectById(orderId);
        order.setOldStatus(order.getOrderStatus());
        order.setOrderStatus(OrderStatusEnum.orderRepeal.getStatus());
        updateOrder(order);

        //修改审核订单状态
        auditService.freezeOrder(orderId);
    }

    /**
     * 解冻注资
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreezeOrder(Long orderId) {
        //TODO 待写
    }

    /**
     * 机器查询数据包
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order findOrderByOrderId(Long orderId) {
        return this.baseMapper.selectById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Order order) {
        saveOrUpdate(order);
    }

    @Override
    public Map<String, Object> findOrderDetailByOrderId(String orderId) {
        return this.baseMapper.findOrderDetailByOrderId(orderId);
    }

}
