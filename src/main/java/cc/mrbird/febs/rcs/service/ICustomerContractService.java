package cc.mrbird.febs.rcs.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.CustomerContract;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户-合同关系表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:46:14
 */
public interface ICustomerContractService extends IService<CustomerContract> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param customerContract customerContract
     * @return IPage<CustomerContract>
     */
    IPage<CustomerContract> findCustomerContracts(QueryRequest request, CustomerContract customerContract);

    /**
     * 查询（所有）
     *
     * @param customerContract customerContract
     * @return List<CustomerContract>
     */
    List<CustomerContract> findCustomerContracts(CustomerContract customerContract);

    /**
     * 新增
     *
     * @param customerContract customerContract
     */
    void createCustomerContract(CustomerContract customerContract);

    /**
     * 修改
     *
     * @param customerContract customerContract
     */
    void updateCustomerContract(CustomerContract customerContract);

    /**
     * 删除
     *
     * @param customerContract customerContract
     */
    void deleteCustomerContract(CustomerContract customerContract);
}
