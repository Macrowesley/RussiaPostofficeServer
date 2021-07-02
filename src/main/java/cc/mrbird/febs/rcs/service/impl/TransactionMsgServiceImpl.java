package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import cc.mrbird.febs.rcs.mapper.TransactionMsgMapper;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
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
public class TransactionMsgServiceImpl extends ServiceImpl<TransactionMsgMapper, TransactionMsg> implements ITransactionMsgService {

    @Autowired
    TransactionMsgMapper transactionMsgMapper;

    @Override
    public IPage<TransactionMsg> findTransactionMsgs(QueryRequest request, TransactionMsg transactionMsg) {
        LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<TransactionMsg> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TransactionMsg> findTransactionMsgs(TransactionMsg transactionMsg) {
	    LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransactionMsg(TransactionMsg transactionMsg) {
        this.save(transactionMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransactionMsg(TransactionMsg transactionMsg) {
        this.saveOrUpdate(transactionMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransactionMsg(TransactionMsg transactionMsg) {
        LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
