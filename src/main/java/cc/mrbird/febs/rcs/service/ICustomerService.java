package cc.mrbird.febs.rcs.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Customer;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:59
 */
public interface ICustomerService extends IService<Customer> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param customer customer
     * @return IPage<Customer>
     */
    IPage<Customer> findCustomers(QueryRequest request, Customer customer);

    /**
     * 查询（所有）
     *
     * @param customer customer
     * @return List<Customer>
     */
    List<Customer> findCustomers(Customer customer);

    /**
     * 新增
     *
     * @param customer customer
     */
    void createCustomer(Customer customer);

    /**
     * 修改
     *
     * @param customer customer
     */
    void updateCustomer(Customer customer);

    /**
     * 删除
     *
     * @param customer customer
     */
    void deleteCustomer(Customer customer);

    Customer getCustomerByContractCode(String contractCode);
}
