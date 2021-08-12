package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.service.ServiceBalanceDTO;
import cc.mrbird.febs.rcs.entity.Balance;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.mapper.BalanceMapper;
import cc.mrbird.febs.rcs.service.IBalanceService;
import cc.mrbird.febs.rcs.service.IContractService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
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

    @Autowired
    BalanceMapper balanceMapper;
    @Autowired
    IContractService contractService;

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
    @Transactional(rollbackFor = RcsApiException.class)
    public void saveBalance(String contractCode, ServiceBalanceDTO serviceBalanceDTO) {
        if (!StringUtils.isEmpty(serviceBalanceDTO.getContractCode()) && !contractCode.equals(serviceBalanceDTO.getContractCode())){
            throw new RcsApiException(RcsApiErrorEnum.ContractNotSame);
        }
        if (!contractService.checkIExist(contractCode)){
            throw new RcsApiException(RcsApiErrorEnum.ContractNotExist);
        }
        if (serviceBalanceDTO.getCurrent() == null || serviceBalanceDTO.getConsolidate() == null){
            throw new RcsApiException(RcsApiErrorEnum.CurrentOrConsolidateIsNull);
        }
        try {
            Balance balance = new Balance();
            BeanUtils.copyProperties(serviceBalanceDTO,balance);
            balance.setFromType(2);
            balance.setCreatedTime(new Date());
            balance.setRussiaTime(DateKit.parseRussiatime(serviceBalanceDTO.getModified()));

            this.save(balance);
            //更新contract的金额
            Contract contract = new Contract();
            contract.setCode(contractCode);
            contract.setCurrent(serviceBalanceDTO.getCurrent());
            contract.setConsolidate(serviceBalanceDTO.getConsolidate());
            contract.setUpdatedTime(DateKit.parseRussiatime(serviceBalanceDTO.getModified()));
            boolean res = contractService.saveOrUpdate(contract);
            log.info("保存balance结束，更新contract金额结果：{}", res);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RcsApiException(RcsApiErrorEnum.SaveBalanceError);
        }
    }

    @Override
    public void saveReturnBalance(String contractCode, ManagerBalanceDTO managerBalanceDTO) {
        if (!contractService.checkIExist(contractCode)){
            throw new RcsApiException(RcsApiErrorEnum.ContractNotExist);
        }
        if (managerBalanceDTO.getCurrent() == null || managerBalanceDTO.getConsolidate() == null){
            throw new RcsApiException(RcsApiErrorEnum.CurrentOrConsolidateIsNull);
        }
        try {
            Balance balance = new Balance();
            BeanUtils.copyProperties(managerBalanceDTO,balance);
            balance.setFromType(1);
            balance.setCreatedTime(new Date());
            balance.setRussiaTime(DateKit.parseRussiatime(managerBalanceDTO.getTimestamp()));
            this.save(balance);

            //更新contract的金额
            Contract contract = new Contract();
            contract.setCode(contractCode);
            contract.setCurrent(managerBalanceDTO.getCurrent());
            contract.setConsolidate(managerBalanceDTO.getConsolidate());
            contract.setUpdatedTime(DateKit.parseRussiatime(managerBalanceDTO.getTimestamp()));
            boolean res = contractService.saveOrUpdate(contract);
            log.info("保存balance结束，更新contract金额结果：{}", res);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RcsApiException(RcsApiErrorEnum.SaveBalanceError);
        }
    }
}
