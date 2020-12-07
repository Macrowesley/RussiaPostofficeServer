package cc.mrbird.febs.order.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.weaver.ast.Or;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public List<OrderVo> findAllOrders(QueryRequest request, OrderVo orderVo);

    public Order findNewestOrderByAcnum(String acnum);
    /**
     * 单纯创建一个订单
     * @param orderVo
     */
    long createOrder(OrderVo orderVo);

    /**
     * 判断这个表头号的最新订单是否已经闭环/撤销，只有闭环/撤销了才能继续操作
     * @param deviceId
     */
    public void checkOrderIsFinish(Long deviceId);

    /**
     * 提交审核
     * @param order
     */
    void submitAuditApply(OrderVo orderVo);

    /**
     * 添加订单的时候就直接发起申请
     * @param orderVo
     */
    void createOrderAndSubmitApply(OrderVo orderVo);

    /**
     * 机器获取数据包
     * @param orderId
     * @return
     */
    Order machineRequestData(String acnum);

    /**
     * 更新机器注资状态
     * @param orderVo
     */
    void updateMachineInjectionStatus(OrderVo orderVo, boolean injectionStatus);

    /**
     * 提交闭环订单申请
     * @param order
     */
    void submitEndOrderApply(OrderVo orderVo);

    /**
     * 注销注资
     * @param order
     */
    void cancelOrder(Long orderId);

    /**
     * 冻结注资
     * @param order
     */
    void freezeOrder(Long orderId);

    /**
     * 解冻注资
     *
     * @param order
     */
    public void unfreezeOrder(Long orderId);

    Order findOrderByOrderId(Long orderId);

    void updateOrder(Order orderVo);

    Map<String, Object> findOrderDetailByOrderId(Long orderId);

    void editOrder(OrderVo order);

    /**
     * 查询到所有过期的订单，然后批量更新过期状态
     * 把过期订单信息，和创建者信息，插入到提醒表中
     */
    void selectAllExpireOrderAndUpdateAndNoticeUser();
}
