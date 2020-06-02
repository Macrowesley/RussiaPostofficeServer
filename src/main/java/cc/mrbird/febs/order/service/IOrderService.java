package cc.mrbird.febs.order.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.weaver.ast.Or;

import java.util.List;

/**
 * 订单表 Service接口
 *
 *
 * @date 2020-05-27 14:56:29
 */
public interface IOrderService extends IService<Order> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param OrderVo OrderVo
     * @return IPage<OrderVo>
     */
    IPage<OrderVo> findPageOrders(QueryRequest request, OrderVo orderVo);


    /**
     * 单纯创建一个订单
     * @param orderVo
     */
    long createOrder(OrderVo orderVo);

    /**
     * 提交审核
     * @param order
     */
    void submitAuditApply(Long orderId);

    /**
     * 添加订单的时候就直接发起申请
     * @param orderVo
     */
    void createOrderAndSubmitApply(OrderVo orderVo);

    /**
     * 提交闭环订单申请
     * @param order
     */
    void submitEndOrderApply(Long orderId);

    /**
     * 撤销注资
     * @param order
     */
    void cancelOrder(Long orderId);

    /**
     * 冻结注资
     * @param order
     */
    void freezeOrder(Long orderId);

    /**
     * 机器查询数据包
     * @param order
     * @return
     */
    Order findOrderByOrderId(Long orderId);


    void updateOrder(Order orderVo);

    /**
     * 判断是否可以修改订单信息
     * @param orderVo
     * @return
     */
    boolean checkCanEditOrder(OrderVo orderVo);

    /**
     * 判断是否可以申请注资
     * @param orderStatus
     * @return
     */
    boolean checkCanApplyInjection(int orderStatus);

    /**
     * 判断是否可以申请闭环
     * @param orderStatus
     * @return
     */
    boolean checkCanApplyClose(int orderStatus);

    /**
     * 判断是否可以点击撤销
     * @param orderStatus
     * @return
     */
    boolean checkCanRepeal(int orderStatus);

    /**
     * 判断是否可以冻结
     * @param orderStatus
     * @return
     */
    boolean checkCanFreeze(int orderStatus);

    /**
     * 判断是否可以修改订单信息
     * @param orderStatus
     * @return
     */
    boolean checkCanEdit(int orderStatus);
}
