package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.mapper.ContractMapper;
import cc.mrbird.febs.rcs.service.IContractService;
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
 * 合同表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    private final ContractMapper contractMapper;

    @Override
    public IPage<Contract> findContracts(QueryRequest request, Contract contract) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Contract> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Contract> findContracts(Contract contract) {
	    LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createContract(Contract contract) {
        this.save(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContract(Contract contract) {
        this.saveOrUpdate(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContract(Contract contract) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
