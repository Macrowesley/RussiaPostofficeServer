package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.vo.ForeseenVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 预算订单 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:44:51
 */
public interface IForeseenService extends IService<Foreseen> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param foreseen foreseen
     * @return IPage<Foreseen>
     */
    IPage<Foreseen> findForeseens(QueryRequest request, Foreseen foreseen);

    /**
     * 查询（所有）
     *
     * @param foreseen foreseen
     * @return List<Foreseen>
     */
    List<Foreseen> findForeseens(Foreseen foreseen);

    /**
     * 新增
     *
     * @param foreseen foreseen
     */
    void createForeseen(Foreseen foreseen);

    /**
     * 修改
     *
     * @param foreseen foreseen
     */
    void updateForeseen(Foreseen foreseen);

    /**
     * 删除
     *
     * @param foreseen foreseen
     */
    void deleteForeseen(Foreseen foreseen);

    /**
     *
     *
     */
    Foreseen getForeseenDetail(String foreseenId);
}
