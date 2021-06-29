package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 交易表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
public interface ITransactionMsgService extends IService<TransactionMsg> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param transactionMsg transactionMsg
     * @return IPage<TransactionMsg>
     */
    IPage<TransactionMsg> findTransactions(QueryRequest request, TransactionMsg transactionMsg);

    /**
     * 查询（所有）
     *
     * @param transactionMsg transactionMsg
     * @return List<TransactionMsg>
     */
    List<TransactionMsg> findTransactions(TransactionMsg transactionMsg);

    /**
     * 新增
     *
     * @param transactionMsg transactionMsg
     */
    void createTransaction(TransactionMsg transactionMsg);

    /**
     * 修改
     *
     * @param transactionMsg transactionMsg
     */
    void updateTransaction(TransactionMsg transactionMsg);

    /**
     * 删除
     *
     * @param transactionMsg transactionMsg
     */
    void deleteTransaction(TransactionMsg transactionMsg);
}
