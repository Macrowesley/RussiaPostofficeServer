package cc.mrbird.febs.rcs.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.ServiceBalanceDTO;
import cc.mrbird.febs.rcs.entity.Balance;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 数据同步记录表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:00
 */
public interface IBalanceService extends IService<Balance> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param balance balance
     * @return IPage<Balance>
     */
    IPage<Balance> findBalances(QueryRequest request, Balance balance);

    /**
     * 查询（所有）
     *
     * @param balance balance
     * @return List<Balance>
     */
    List<Balance> findBalances(Balance balance);

    /**
     * 新增
     *
     * @param balance balance
     */
    void createBalance(Balance balance);

    /**
     * 修改
     *
     * @param balance balance
     */
    void updateBalance(Balance balance);

    /**
     * 删除
     *
     * @param balance balance
     */
    void deleteBalance(Balance balance);

    void saveBalance(String contractId, ServiceBalanceDTO serviceBalanceDTO);
}
