package cc.mrbird.febs.order.mapper;

import cc.mrbird.febs.order.entity.OrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cc.mrbird.febs.order.entity.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单表 Mapper
 *
 *
 * @date 2020-05-27 14:56:29
 */
public interface OrderMapper extends BaseMapper<Order> {


    IPage<OrderVo> selectByDeviceId(Page<OrderVo> page, @Param("curUserId") long curUserId, @Param("order") OrderVo order);

    List<OrderVo> selectByDeviceId(@Param("curUserId") long curUserId, @Param("order") OrderVo order);

    IPage<OrderVo> selectByUserId(Page<OrderVo> page, @Param("curUserId") long curUserId, @Param("order") OrderVo orderVo);
}
