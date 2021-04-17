package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.mapper.TaxMapper;
import cc.mrbird.febs.rcs.service.ITaxService;
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
 * 税率表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TaxServiceImpl extends ServiceImpl<TaxMapper, Tax> implements ITaxService {

    private final TaxMapper taxMapper;

    @Override
    public IPage<Tax> findTaxs(QueryRequest request, Tax tax) {
        LambdaQueryWrapper<Tax> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Tax> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Tax> findTaxs(Tax tax) {
	    LambdaQueryWrapper<Tax> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTax(Tax tax) {
        this.save(tax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTax(Tax tax) {
        this.saveOrUpdate(tax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTax(Tax tax) {
        LambdaQueryWrapper<Tax> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
