package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * 预算订单产品 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:44:55
 */
public interface IForeseenProductService extends IService<ForeseenProduct> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param foreseenProduct foreseenProduct
     * @return IPage<ForeseenProduct>
     */
    IPage<ForeseenProduct> findForeseenProducts(QueryRequest request, ForeseenProduct foreseenProduct);

    /**
     * 查询（所有）
     *
     * @param foreseenProduct foreseenProduct
     * @return List<ForeseenProduct>
     */
    List<ForeseenProduct> findForeseenProducts(ForeseenProduct foreseenProduct);

    /**
     * 新增
     *
     * @param foreseenProduct foreseenProduct
     */
    void createForeseenProduct(ForeseenProduct foreseenProduct);

    /**
     * 修改
     *
     * @param foreseenProduct foreseenProduct
     */
    void updateForeseenProduct(ForeseenProduct foreseenProduct);

    /**
     * 删除
     *
     * @param foreseenProduct foreseenProduct
     */
    void deleteForeseenProduct(ForeseenProduct foreseenProduct);

    ArrayList<ForeseenProduct> getByPrintJobId(int id);
}
