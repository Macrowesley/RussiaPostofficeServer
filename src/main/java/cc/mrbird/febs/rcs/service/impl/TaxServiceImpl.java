package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.dto.service.PostalProductDTO;
import cc.mrbird.febs.rcs.dto.service.TaxRateDTO;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.PostalProduct;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.entity.TaxRate;
import cc.mrbird.febs.rcs.mapper.TaxMapper;
import cc.mrbird.febs.rcs.service.IPostalProductService;
import cc.mrbird.febs.rcs.service.ITaxRateService;
import cc.mrbird.febs.rcs.service.ITaxService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 税率表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TaxServiceImpl extends ServiceImpl<TaxMapper, Tax> implements ITaxService {
    private final IPostalProductService postalProductService;
    private final ITaxRateService taxRateService;
    private final TaxMapper taxMapper;
    private final IDeviceService deviceService;

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

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public void saveTaxVersion(TaxVersionDTO taxVersionDTO) {
        log.info("保存tax开始");
        long t1 = System.currentTimeMillis();
        Tax tax = new Tax();
        BeanUtils.copyProperties(taxVersionDTO,tax);
        this.save(tax);

        List<TaxRate> taxRates = new ArrayList<>();

        PostalProductDTO[] productDTOS = taxVersionDTO.getProducts();
        for (PostalProductDTO postalProductDto : productDTOS){
            //循环
            PostalProduct postalProduct = new PostalProduct();
            BeanUtils.copyProperties(postalProductDto, postalProduct);
            postalProduct.setTaxId(tax.getId());
            postalProduct.setCreatedTime(new Date());
            postalProduct.setUpdatedTime(new Date());
            postalProductService.save(postalProduct);

            TaxRateDTO[] taxRateDTOS = postalProductDto.getTaxes();
            for (TaxRateDTO taxRateDTO : taxRateDTOS){
                //循环
                TaxRate taxRate = new TaxRate();
                taxRate.setPostalProductId(postalProduct.getId());
                BeanUtils.copyProperties(taxRateDTO, taxRate);
                taxRates.add(taxRate);
            }
        }

        taxRateService.saveBatch(taxRates);

        //更新所有device的taxIsUpdate 全都改成0
        deviceService.updateLastestTaxVersionUpdateStatuts();

        log.info("保存tax结束，耗时：{}", (System.currentTimeMillis() - t1));
    }

    @Override
    public Tax getLastestTax() {
        LambdaQueryWrapper<Tax> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tax::getId).last("limit 1");
        return this.getOne(queryWrapper);
    }
}
