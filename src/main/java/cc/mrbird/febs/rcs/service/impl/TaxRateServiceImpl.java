package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.TaxRate;
import cc.mrbird.febs.rcs.mapper.TaxRateMapper;
import cc.mrbird.febs.rcs.service.ITaxRateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 税率细节表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:55
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TaxRateServiceImpl extends ServiceImpl<TaxRateMapper, TaxRate> implements ITaxRateService {

    private final TaxRateMapper taxRateMapper;

    @Override
    public IPage<TaxRate> findTaxRates(QueryRequest request, TaxRate taxRate) {
        LambdaQueryWrapper<TaxRate> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<TaxRate> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TaxRate> findTaxRates(TaxRate taxRate) {
	    LambdaQueryWrapper<TaxRate> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTaxRate(TaxRate taxRate) {
        this.save(taxRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaxRate(TaxRate taxRate) {
        this.saveOrUpdate(taxRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTaxRate(TaxRate taxRate) {
        LambdaQueryWrapper<TaxRate> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
