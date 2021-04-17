package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import cc.mrbird.febs.rcs.mapper.ForeseenProductMapper;
import cc.mrbird.febs.rcs.service.IForeseenProductService;
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
 * 预算订单产品 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:44:55
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ForeseenProductServiceImpl extends ServiceImpl<ForeseenProductMapper, ForeseenProduct> implements IForeseenProductService {

    private final ForeseenProductMapper foreseenProductMapper;

    @Override
    public IPage<ForeseenProduct> findForeseenProducts(QueryRequest request, ForeseenProduct foreseenProduct) {
        LambdaQueryWrapper<ForeseenProduct> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<ForeseenProduct> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ForeseenProduct> findForeseenProducts(ForeseenProduct foreseenProduct) {
	    LambdaQueryWrapper<ForeseenProduct> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForeseenProduct(ForeseenProduct foreseenProduct) {
        this.save(foreseenProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForeseenProduct(ForeseenProduct foreseenProduct) {
        this.saveOrUpdate(foreseenProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForeseenProduct(ForeseenProduct foreseenProduct) {
        LambdaQueryWrapper<ForeseenProduct> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
