package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TransactionData;
import cc.mrbird.febs.rcs.mapper.TransactionDataMapper;
import cc.mrbird.febs.rcs.service.ITransactionDataService;
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
 * 交易数据表【待定】 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:46:25
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TransactionDataServiceImpl extends ServiceImpl<TransactionDataMapper, TransactionData> implements ITransactionDataService {

    private final TransactionDataMapper transactionDataMapper;

    @Override
    public IPage<TransactionData> findTransactionDatas(QueryRequest request, TransactionData transactionData) {
        LambdaQueryWrapper<TransactionData> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<TransactionData> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TransactionData> findTransactionDatas(TransactionData transactionData) {
	    LambdaQueryWrapper<TransactionData> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransactionData(TransactionData transactionData) {
        this.save(transactionData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransactionData(TransactionData transactionData) {
        this.saveOrUpdate(transactionData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransactionData(TransactionData transactionData) {
        LambdaQueryWrapper<TransactionData> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
