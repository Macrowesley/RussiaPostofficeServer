package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Customer;
import cc.mrbird.febs.rcs.mapper.CustomerMapper;
import cc.mrbird.febs.rcs.service.ICustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:59
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public IPage<Customer> findCustomers(QueryRequest request, Customer customer) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Customer> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Customer> findCustomers(Customer customer) {
	    LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(Customer customer) {
        this.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(Customer customer) {
        this.saveOrUpdate(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Customer customer) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    public String getUserIdByContractId(String contractId) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getContractId,contractId);
        return this.baseMapper.selectOne(wrapper).getId();
    }
}
