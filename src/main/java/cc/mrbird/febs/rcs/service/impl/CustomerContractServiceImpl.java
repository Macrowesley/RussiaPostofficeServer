package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.CustomerContract;
import cc.mrbird.febs.rcs.mapper.CustomerContractMapper;
import cc.mrbird.febs.rcs.service.ICustomerContractService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户-合同关系表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:46:14
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CustomerContractServiceImpl extends ServiceImpl<CustomerContractMapper, CustomerContract> implements ICustomerContractService {

    private final CustomerContractMapper customerContractMapper;

    @Override
    public IPage<CustomerContract> findCustomerContracts(QueryRequest request, CustomerContract customerContract) {
        LambdaQueryWrapper<CustomerContract> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<CustomerContract> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<CustomerContract> findCustomerContracts(CustomerContract customerContract) {
	    LambdaQueryWrapper<CustomerContract> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomerContract(CustomerContract customerContract) {
        this.save(customerContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomerContract(CustomerContract customerContract) {
        this.saveOrUpdate(customerContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerContract(CustomerContract customerContract) {
        LambdaQueryWrapper<CustomerContract> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
