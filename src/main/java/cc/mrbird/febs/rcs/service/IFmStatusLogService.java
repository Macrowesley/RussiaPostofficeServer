package cc.mrbird.febs.rcs.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.FmStatusLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 机器状态变更表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:44:22
 */
public interface IFmStatusLogService extends IService<FmStatusLog> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param fmStatusLog fmStatusLog
     * @return IPage<FmStatusLog>
     */
    IPage<FmStatusLog> findFmStatusLogs(QueryRequest request, FmStatusLog fmStatusLog);

    /**
     * 查询（所有）
     *
     * @param fmStatusLog fmStatusLog
     * @return List<FmStatusLog>
     */
    List<FmStatusLog> findFmStatusLogs(FmStatusLog fmStatusLog);

    /**
     * 新增
     *
     * @param fmStatusLog fmStatusLog
     */
    void createFmStatusLog(FmStatusLog fmStatusLog);

    /**
     * 修改
     *
     * @param fmStatusLog fmStatusLog
     */
    void updateFmStatusLog(FmStatusLog fmStatusLog);

    /**
     * 删除
     *
     * @param fmStatusLog fmStatusLog
     */
    void deleteFmStatusLog(FmStatusLog fmStatusLog);
}
