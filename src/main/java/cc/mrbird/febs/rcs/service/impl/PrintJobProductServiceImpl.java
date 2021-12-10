package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PrintJobProduct;
import cc.mrbird.febs.rcs.mapper.PrintJobProductMapper;
import cc.mrbird.febs.rcs.service.IPrintJobProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrintJobProductServiceImpl extends ServiceImpl<PrintJobProductMapper, PrintJobProduct> implements IPrintJobProductService {
    @Autowired
    PrintJobProductMapper printJobProductMapper;
    /**
     * 查询（分页）
     *
     * @param request         QueryRequest
     * @param printJobProduct printJobProduct
     * @return IPage<PrintJobProduct>
     */
    @Override
    public IPage<PrintJobProduct> findPrintJobProducts(QueryRequest request, PrintJobProduct printJobProduct) {
        LambdaQueryWrapper<PrintJobProduct> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PrintJobProduct> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /**
     * 查询（所有）
     *
     * @param printJobProduct printJobProduct
     * @return List<PrintJobProduct>
     */
    @Override
    public List<PrintJobProduct> findPrintJobProducts(PrintJobProduct printJobProduct) {
        LambdaQueryWrapper<PrintJobProduct> queryWrapper = new LambdaQueryWrapper<>();
        if (printJobProduct.getPrintJobId() != 0){
            queryWrapper.eq(PrintJobProduct::getPrintJobId, printJobProduct.getPrintJobId());
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 新增
     *
     * @param printJobProduct printJobProduct
     */
    @Override
    public void createPrintJobProduct(PrintJobProduct printJobProduct) {
        this.save(printJobProduct);
    }

    /**
     * 修改
     *
     * @param printJobProduct printJobProduct
     */
    @Override
    public void updatePrintJobProduct(PrintJobProduct printJobProduct) {
        this.saveOrUpdate(printJobProduct);
    }

    /**
     * 删除
     *
     * @param printJobProduct printJobProduct
     */
    @Override
    public void deletePrintJobProduct(PrintJobProduct printJobProduct) {
        LambdaQueryWrapper<PrintJobProduct> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }
}
