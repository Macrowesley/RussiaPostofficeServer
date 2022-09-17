package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.mapper.TransactionMapper;
import cc.mrbird.febs.rcs.service.ITransactionService;
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
 * 交易表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

    @Autowired
    TransactionMapper transactionMapper;

    @Override
    public IPage<Transaction> findTransactions(QueryRequest request, Transaction transaction) {
        LambdaQueryWrapper<Transaction> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Transaction> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Transaction> findTransactions(Transaction transaction) {
	    LambdaQueryWrapper<Transaction> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransaction(Transaction transaction) {
        this.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransaction(Transaction transaction) {
        this.saveOrUpdate(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransaction(Transaction transaction) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    public Transaction getTransactionDetail(String transactionId) {
        LambdaQueryWrapper<Transaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Transaction::getId,transactionId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
