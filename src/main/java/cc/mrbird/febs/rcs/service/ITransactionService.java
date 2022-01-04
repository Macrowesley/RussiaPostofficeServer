package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Transaction;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 交易表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
public interface ITransactionService extends IService<Transaction> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param transaction transaction
     * @return IPage<Transaction>
     */
    IPage<Transaction> findTransactions(QueryRequest request, Transaction transaction);

    /**
     * 查询（所有）
     *
     * @param transaction transaction
     * @return List<Transaction>
     */
    List<Transaction> findTransactions(Transaction transaction);

    /**
     * 新增
     *
     * @param transaction transaction
     */
    void createTransaction(Transaction transaction);

    /**
     * 修改
     *
     * @param transaction transaction
     */
    void updateTransaction(Transaction transaction);

    /**
     * 删除
     *
     * @param transaction transaction
     */
    void deleteTransaction(Transaction transaction);

    Transaction getTransactionDetail(String transactionId);
}
