package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;

import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.mapper.OrderMapper;
import cc.mrbird.febs.order.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.order.entity.Order;
import java.util.List;

/**
 * 订单表 Service实现
 *
 *
 * @date 2020-05-27 14:56:29
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final OrderMapper orderMapper;

    @Override
    public IPage<Order> findOrders(QueryRequest request, Order order) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Order> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Order> findOrders(Order order) {
	    LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
        this.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Order order) {
        this.saveOrUpdate(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Order order) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
