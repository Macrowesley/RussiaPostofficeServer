package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Statistics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【待定】 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:39
 */
public interface IStatisticsService extends IService<Statistics> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param statistics statistics
     * @return IPage<Statistics>
     */
    IPage<Statistics> findStatisticss(QueryRequest request, Statistics statistics);

    /**
     * 查询（所有）
     *
     * @param statistics statistics
     * @return List<Statistics>
     */
    List<Statistics> findStatisticss(Statistics statistics);

    /**
     * 新增
     *
     * @param statistics statistics
     */
    void createStatistics(Statistics statistics);

    /**
     * 修改
     *
     * @param statistics statistics
     */
    void updateStatistics(Statistics statistics);

    /**
     * 删除
     *
     * @param statistics statistics
     */
    void deleteStatistics(Statistics statistics);
}
