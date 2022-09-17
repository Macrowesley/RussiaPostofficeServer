package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmRespDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductPcRespDTO;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import cc.mrbird.febs.rcs.mapper.ForeseenProductMapper;
import cc.mrbird.febs.rcs.service.IForeseenProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    ForeseenProductMapper foreseenProductMapper;

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
        if (foreseenProduct.getPrintJobId() != 0){
            queryWrapper.eq(ForeseenProduct::getPrintJobId, foreseenProduct.getPrintJobId());
        }
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

        if (foreseenProduct.getPrintJobId() != null && foreseenProduct.getPrintJobId() != 0){
            wrapper.eq(ForeseenProduct::getPrintJobId, foreseenProduct.getPrintJobId());
        }
	    this.remove(wrapper);
	}

    @Override
    public ArrayList<ForeseenProduct> getByPrintJobId(int id) {
        LambdaQueryWrapper<ForeseenProduct> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(ForeseenProduct::getPrintJobId, id);

        return (ArrayList<ForeseenProduct>) this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据订单id获取对应的产品列表，包含广告图片信息
     *
     * @param printJobId
     * @return
     */
    @Override
    public List<ForeseenProductFmRespDTO> selectFmProductAdList(Integer printJobId) {
        return this.baseMapper.selectProductAdList(printJobId);
    }

    @Override
    public List<ForeseenProductPcRespDTO> selectPcProductAdList(Integer printJobId) {
        List<ForeseenProductFmRespDTO> foreseenProductFmRespList = selectFmProductAdList(printJobId);
        List<ForeseenProductPcRespDTO> collect = foreseenProductFmRespList.stream().map(bean -> {
            ForeseenProductPcRespDTO target = new ForeseenProductPcRespDTO();
            BeanUtils.copyProperties(bean, target);
            return target;
        }).collect(Collectors.toList());
        return collect;
    }
}
