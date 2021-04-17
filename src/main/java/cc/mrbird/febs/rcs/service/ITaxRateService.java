package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TaxRate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 税率细节表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:55
 */
public interface ITaxRateService extends IService<TaxRate> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param taxRate taxRate
     * @return IPage<TaxRate>
     */
    IPage<TaxRate> findTaxRates(QueryRequest request, TaxRate taxRate);

    /**
     * 查询（所有）
     *
     * @param taxRate taxRate
     * @return List<TaxRate>
     */
    List<TaxRate> findTaxRates(TaxRate taxRate);

    /**
     * 新增
     *
     * @param taxRate taxRate
     */
    void createTaxRate(TaxRate taxRate);

    /**
     * 修改
     *
     * @param taxRate taxRate
     */
    void updateTaxRate(TaxRate taxRate);

    /**
     * 删除
     *
     * @param taxRate taxRate
     */
    void deleteTaxRate(TaxRate taxRate);
}
