package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Balance;
import cc.mrbird.febs.rcs.mapper.BalanceMapper;
import cc.mrbird.febs.rcs.service.IBalanceService;
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
 * 数据同步记录表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:00
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {

    private final BalanceMapper balanceMapper;

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
}
