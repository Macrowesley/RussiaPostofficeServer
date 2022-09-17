package cc.mrbird.febs.order.service;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.weaver.ast.Or;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;


public interface IOrderService extends IService<Order> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
//    Order findByName(String username);

    /**
     * 查找用户详细信息
     *
     * @param request request
     * @param order    用户对象，用于传递查询条件
     * @return IPage
     */
//    IPage<User> findUserDetailList(User user, QueryRequest request);
    IPage<Order> findOrderDetailList(Order order, QueryRequest request);

    /**
     * 通过用户名查找用户详细信息
     *
     * @param username 用户名
     * @return 用户信息
     */
//    User findUserDetailList(String username);

    /**
     * 新增用户
     *
     * @param order order
     */
    void createOrder(Order order);

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
//    void deleteUsers(String[] userIds);

    /**
     * 修改用户
     *
     * @param user user
     */
//    void updateUser(User user);

}
