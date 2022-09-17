package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TransactionData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 交易数据表【待定】 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:46:25
 */
public interface ITransactionDataService extends IService<TransactionData> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param transactionData transactionData
     * @return IPage<TransactionData>
     */
    IPage<TransactionData> findTransactionDatas(QueryRequest request, TransactionData transactionData);

    /**
     * 查询（所有）
     *
     * @param transactionData transactionData
     * @return List<TransactionData>
     */
    List<TransactionData> findTransactionDatas(TransactionData transactionData);

    /**
     * 新增
     *
     * @param transactionData transactionData
     */
    void createTransactionData(TransactionData transactionData);

    /**
     * 修改
     *
     * @param transactionData transactionData
     */
    void updateTransactionData(TransactionData transactionData);

    /**
     * 删除
     *
     * @param transactionData transactionData
     */
    void deleteTransactionData(TransactionData transactionData);
}
