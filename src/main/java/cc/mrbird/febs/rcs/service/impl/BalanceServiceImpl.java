package cc.mrbird.febs.rcs.service.impl;
import java.util.Date;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.ServiceBalanceDTO;
import cc.mrbird.febs.rcs.entity.Balance;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.mapper.BalanceMapper;
import cc.mrbird.febs.rcs.service.IBalanceService;
import cc.mrbird.febs.rcs.service.IContractService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据同步记录表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {

    private final BalanceMapper balanceMapper;
    private final IContractService contractService;

    @Override
    public IPage<Balance> findBalances(QueryRequest request, Balance balance) {
        LambdaQueryWrapper<Balance> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Balance> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Balance> findBalances(Balance balance) {
	    LambdaQueryWrapper<Balance> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBalance(Balance balance) {
        this.save(balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBalance(Balance balance) {
        this.saveOrUpdate(balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBalance(Balance balance) {
        LambdaQueryWrapper<Balance> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public void saveBalance(String contractId, ServiceBalanceDTO serviceBalanceDTO) {
        Balance balance = new Balance();
        BeanUtils.copyProperties(serviceBalanceDTO,balance);
        balance.setFromType(1);
        balance.setCreatedTime(new Date());
        balance.setUpdatedTime(new Date());

        /**
         * 当前可用资金（包括持有）
         */
        @NonNull
        Double current;

        /**
         * 当前余额（仅事实）
         */
        @NonNull
        Double consolidate;


        this.save(balance);
        //更新contract的金额
        Contract contract = contractService.getByConractId(contractId);
        contract.setCurrent(serviceBalanceDTO.getCurrent());
        contract.setConsolidate(serviceBalanceDTO.getConsolidate());
        contract.setUpdatedTime(new Date());
        boolean res = contractService.saveOrUpdate(contract);
        log.info("保存balance结束，更新contract金额结果：{}", res);
    }
}
