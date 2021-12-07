package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PrintJobProduct;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打印任务表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:44:46
 */
public interface IPrintJobProductService extends IService<PrintJobProduct> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param printJobProduct printJobProduct
     * @return IPage<PrintJobProduct>
     */
    IPage<PrintJobProduct> findPrintJobProducts(QueryRequest request, PrintJobProduct printJobProduct);

    /**
     * 查询（所有）
     *
     * @param printJobProduct printJobProduct
     * @return List<PrintJobProduct>
     */
    List<PrintJobProduct> findPrintJobProducts(PrintJobProduct printJobProduct);

    /**
     * 新增
     *
     * @param printJobProduct printJobProduct
     */
    void createPrintJobProduct(PrintJobProduct printJobProduct);

    /**
     * 修改
     *
     * @param printJobProduct printJobProduct
     */
    void updatePrintJobProduct(PrintJobProduct printJobProduct);

    /**
     * 删除
     *
     * @param printJobProduct printJobProduct
     */
    void deletePrintJobProduct(PrintJobProduct printJobProduct);


}
