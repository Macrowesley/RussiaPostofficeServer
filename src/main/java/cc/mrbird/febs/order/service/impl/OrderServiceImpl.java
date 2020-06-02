package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.service.impl.AuditServiceImpl;
import cc.mrbird.febs.common.entity.*;

import cc.mrbird.febs.common.enums.AuditStatusEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.mapper.OrderMapper;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.order.entity.Order;

import java.util.Date;
import java.util.List;

/**
 * 订单表 Service实现
 *
 * @date 2020-05-27 14:56:29
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final OrderMapper orderMapper;
    private final AuditServiceImpl auditService;

    @Override
    public IPage<OrderVo> findPageOrders(QueryRequest request, OrderVo orderVo) {

        Page<OrderVo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "order_id", FebsConstant.ORDER_DESC, false);

        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();
        long curUserId = curUser.getUserId();
        if (roleId.equals(RoleType.organizationManager) || roleId.equals(RoleType.systemManager)) {
            //根据设备id获取order列表
            return this.orderMapper.selectByDeviceId(page, curUserId, orderVo);
        } else {
            //根据userid获取order列表
            return this.orderMapper.selectByUserId(page, curUserId, orderVo);
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
        orderVo.setApplyUserId(FebsUtil.getCurrentUser().getUserId());
        orderVo.setOrderStatus(OrderStatusEnum.createOrder.getStatus());
        orderVo.setCreateTime(new Date());
        this.baseMapper.insert(orderVo);
        return orderVo.getOrderId();
    }

    /**
     * 提交审核
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAuditApply(Long orderId) {
        Order order = findOrderByOrderId(orderId);

        //修改订单状态
        order.setOrderStatus(OrderStatusEnum.beginApply.getStatus());
        updateOrder(order);

        //添加注资申请
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
        orderVo.setApplyUserId(FebsUtil.getCurrentUser().getUserId());
        orderVo.setOrderStatus(OrderStatusEnum.beginApply.getStatus());
        orderVo.setCreateTime(new Date());
        this.baseMapper.insert(orderVo);

        //添加注资申请
        auditService.createAudit(orderVo, AuditType.injection);
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

    /**
     * 判断是否可以修改订单信息
     *
     * @param orderVo
     * @return
     */
    @Override
    public boolean checkCanEditOrder(OrderVo orderVo) {
        return false;
    }

    /**
     * 判断是否可以申请注资
     *
     * @param orderStatus
     * @return
     */
    @Override
    public boolean checkCanApplyInjection(int orderStatus) {
        return false;
    }

    /**
     * 判断是否可以申请闭环
     *
     * @param orderStatus
     * @return
     */
    @Override
    public boolean checkCanApplyClose(int orderStatus) {
        return false;
    }

    /**
     * 判断是否可以点击撤销
     *
     * @param orderStatus
     * @return
     */
    @Override
    public boolean checkCanRepeal(int orderStatus) {
        return false;
    }

    /**
     * 判断是否可以冻结
     *
     * @param orderStatus
     * @return
     */
    @Override
    public boolean checkCanFreeze(int orderStatus) {
        return false;
    }

    /**
     * 判断是否可以修改订单信息
     *
     * @param orderStatus
     * @return
     */
    @Override
    public boolean checkCanEdit(int orderStatus) {
        return false;
    }
}
