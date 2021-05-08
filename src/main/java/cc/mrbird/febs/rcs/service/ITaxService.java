package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.Tax;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 税率表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
public interface ITaxService extends IService<Tax> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param tax tax
     * @return IPage<Tax>
     */
    IPage<Tax> findTaxs(QueryRequest request, Tax tax);

    /**
     * 查询（所有）
     *
     * @param tax tax
     * @return List<Tax>
     */
    List<Tax> findTaxs(Tax tax);

    /**
     * 新增
     *
     * @param tax tax
     */
    void createTax(Tax tax);

    /**
     * 修改
     *
     * @param tax tax
     */
    void updateTax(Tax tax);

    /**
     * 删除
     *
     * @param tax tax
     */
    void deleteTax(Tax tax);

    void saveTaxVersion(TaxVersionDTO taxVersionDTO);

    Tax getLastTax();
}
