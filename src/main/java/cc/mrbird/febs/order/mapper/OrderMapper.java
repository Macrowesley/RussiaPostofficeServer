package cc.mrbird.febs.order.mapper;

import cc.mrbird.febs.order.entity.OrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cc.mrbird.febs.order.entity.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单表 Mapper
 *
 *
 * @date 2020-05-27 14:56:29
 */
public interface OrderMapper extends BaseMapper<Order> {


    /**
     * 系统管理员查看订单列表
     * @param page
     * @param orderVo
     * @return
     */
    IPage<OrderVo> selectBySystemManager(Page<OrderVo> page, @Param("order") OrderVo orderVo);
    List<OrderVo> selectBySystemManager(@Param("order") OrderVo orderVo);

    /**
     * 机构管理员查看订单列表
     * @param page
     * @param curUserId
     * @param order
     * @return
     */
    IPage<OrderVo> selectByOrganizationManager(Page<OrderVo> page, @Param("curUserId") long curUserId, @Param("order") OrderVo order);
    List<OrderVo> selectByOrganizationManager(@Param("curUserId") long curUserId, @Param("order") OrderVo order);

    /**
     * 设备管理员查看订单列表
     * @param page
     * @param curUserId
     * @param orderVo
     * @return
     */
    IPage<OrderVo> selectByUserId(Page<OrderVo> page, @Param("curUserId") long curUserId, @Param("order") OrderVo orderVo);
    List<OrderVo> selectByUserId(@Param("curUserId") long curUserId, @Param("order") OrderVo orderVo);

    Map<String, Object> findOrderDetailByOrderId(String orderId);
}
