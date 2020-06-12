package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.entity.*;

import cc.mrbird.febs.common.enums.OrderBtnEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.notice.entity.Notice;
import cc.mrbird.febs.notice.service.INoticeService;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.mapper.OrderMapper;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.order.utils.StatusUtils;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.unit.DataUnit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final INoticeService noticeService;

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
        checkOrderIsFinish(orderVo.getDeviceId());

        //验证金额
        checkAmountIsOk(orderVo);

        orderVo.setOrderStatus(OrderStatusEnum.createOrder.getStatus());
        addOtherParams(orderVo);
        this.baseMapper.insert(orderVo);
        return orderVo.getOrderId();
    }

    /**
     * 判断这个表头号的最新订单是否已经闭环/撤销，只有闭环/撤销了才能继续操作
     *
     * @param deviceId
     */
    @Override
    public void checkOrderIsFinish(Long deviceId) {
        Order oldOrder = findNewestOrderByDeviceId(deviceId);
        if (oldOrder != null) {
            OrderStatusEnum statusEnum = OrderStatusEnum.getByStatus(oldOrder.getOrderStatus());
            if (!(statusEnum == OrderStatusEnum.machineInjectionSuccess || statusEnum == OrderStatusEnum.orderRepeal)) {
                throw new FebsException("订单没有闭环/撤销，无法操作");
            }
        }
    }

    /**
     * 根据设备Id获取最新的订单
     *
     * @param deviceId
     * @return
     */
    private Order findNewestOrderByDeviceId(Long deviceId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getDeviceId, deviceId);
        queryWrapper.last("limit 1");
        queryWrapper.orderByDesc(Order::getOrderId);
        List<Order> list = this.baseMapper.selectList(queryWrapper);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 修改订单
     *
     * @param orderVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editOrder(OrderVo orderVo) {
        //验证金额
        Order order = findOrderByOrderId(orderVo.getOrderId());
        orderVo.setDeviceId(order.getDeviceId());

        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.editBtn, order.getOrderStatus());
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
        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.submitInjectionBtn, order.getOrderStatus());

        //修改订单状态
        order.setOrderStatus(OrderStatusEnum.auditIng.getStatus());
        updateOrder(order);

        //添加注资申请
        BeanUtils.copyProperties(order, orderVo);
        auditService.createAudit(orderVo, AuditType.injection);
    }


    /**
     * 提交闭环订单申请
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitEndOrderApply(OrderVo orderVo) {
        Order order = findOrderByOrderId(orderVo.getOrderId());

        //验证是否可以执行
        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.submitCloseBtn, order.getOrderStatus());

        //修改订单状态
        order.setOrderStatus(OrderStatusEnum.orderCloseApplyIng.getStatus());
        updateOrder(order);

        //添加闭环申请
        BeanUtils.copyProperties(order, orderVo);
        auditService.createAudit(orderVo, AuditType.closedCycle);
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
     * @param injectionStatus 0 失败 1成功
     */
    @Override
    public void updateMachineInjectionStatus(OrderVo orderVo, boolean injectionStatus) {
        Order order = findOrderByOrderId(orderVo.getOrderId());

        if (injectionStatus) {
            order.setIsExpire("0");
            order.setOrderStatus(OrderStatusEnum.machineInjectionSuccess.getStatus());
        } else {
            order.setOrderStatus(OrderStatusEnum.machineInjectionFail.getStatus());
        }
        updateOrder(order);
    }


    /**
     * 注销注资
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Order order = findOrderByOrderId(orderId);
        //验证是否可以执行
        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.cancelBtn, order.getOrderStatus());

        //修改订单状态
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orderId);
        orderVo.setOrderStatus(OrderStatusEnum.orderRepeal.getStatus());
        orderVo.setIsExpire("0");
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

        //验证是否可以执行
        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.freezeBtn, order.getOrderStatus());

        order.setOldStatus(order.getOrderStatus());
        order.setOrderStatus(OrderStatusEnum.orderFreeze.getStatus());
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
        Order order = this.baseMapper.selectById(orderId);

        //验证是否可以执行
        StatusUtils.checkOrderBtnPermissioin(OrderBtnEnum.unfreezeBtn, order.getOrderStatus());

        order.setOrderStatus(order.getOldStatus());
        order.setOldStatus(null);
        updateOrder(order);

        //修改审核订单状态
        auditService.unFreezeOrder(orderId);
    }


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

    /**
     * 查询到所有过期的订单，然后批量更新过期状态
     * 把过期订单信息，和创建者信息，插入到提醒表中
     */
    @Override
    public void selectAllExpireOrderAndUpdateAndNoticeUser() {
        log.info("查询到所有过期的订单");
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        //不过期  没有注销  没有闭环   到期时间大于现在时间
        queryWrapper.ne(Order::getOrderStatus, OrderStatusEnum.orderRepeal.getStatus());
        queryWrapper.ne(Order::getOrderStatus, OrderStatusEnum.machineInjectionSuccess.getStatus());
        queryWrapper.ne(Order::getIsExpire, "1");
//        queryWrapper.le(Order::getEndTime, DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        queryWrapper.le(Order::getEndTime, DateUtil.getDateFormat(DateUtil.getDateAfter(new Date(), 7), DateUtil.FULL_TIME_SPLIT_PATTERN));
        List<Order> orderList = baseMapper.selectList(queryWrapper);
        List<Order> list = orderList.stream().map(order -> {
            order.setIsExpire("1");
//            order.setOrderStatus(OrderStatusEnum.orderFreeze.getStatus());
            return order;
        }).collect(Collectors.toList());

        List<Notice> noticeList = new ArrayList<>();
        list.stream().forEach(order -> {
                    Notice notice = new Notice();
                    notice.setUserId(order.getApplyUserId());
                    notice.setOrderId(order.getOrderId());
                    notice.setDeviceId(order.getDeviceId());
                    notice.setOrderNumber(order.getOrderNumber());
                    notice.setAmount(order.getAmount());
                    notice.setIsRead("0");
                    notice.setContent("过期了");
                    notice.setCreateTime(new Date());
                    noticeList.add(notice);
                }
        );

        saveOrUpdateBatch(list);

        noticeService.saveOrUpdateBatch(noticeList);
    }
}
