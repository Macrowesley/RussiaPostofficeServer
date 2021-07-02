package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.service.RateDetailDTO;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.PostalProduct;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.mapper.TaxMapper;
import cc.mrbird.febs.rcs.service.IPostalProductService;
import cc.mrbird.febs.rcs.service.ITaxRateService;
import cc.mrbird.febs.rcs.service.ITaxService;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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
    @Autowired
    IPostalProductService postalProductService;
    @Autowired
    ITaxRateService taxRateService;
    @Autowired
    TaxMapper taxMapper;
    @Autowired
    IDeviceService deviceService;

    @Autowired
    RedisService redisService;

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
    @Transactional(rollbackFor = RcsApiException.class)
    public void saveTaxVersion(TaxVersionDTO taxVersionDTO) {
        try {
            log.info("保存tax开始");
            long t1 = System.currentTimeMillis();

            String savePath = "D:\\PostmartOfficeServiceFile\\tax\\" + DateKit.getNowDateToFileName() + ".json";

            Tax tax = new Tax();
            BeanUtils.copyProperties(taxVersionDTO,tax);
            tax.setSavePath(savePath);
            tax.setApplyDate(DateKit.parseRussiatime(taxVersionDTO.getApplyDate()));
            tax.setPublishDate(DateKit.parseRussiatime(taxVersionDTO.getPublishDate()));
            tax.setModified(DateKit.parseRussiatime(taxVersionDTO.getModified()));
            tax.setCreatedDate(new Date());
//            tax.setCreatedDate(DateKit.parseRussiatime(taxVersionDTO.getCreateDate()));
            this.save(tax);

            RateDetailDTO[] rateDetailDTOS = taxVersionDTO.getDetails();
            List<PostalProduct> postalProductList = new ArrayList<>();
            for (RateDetailDTO item : rateDetailDTOS){

                //暂时数据库文件只保存postalProduct信息，其他就不存入数据库中了，但是整个文件保存在了D盘中
                PostalProduct postalProduct = new PostalProduct();
                BeanUtils.copyProperties(item.getProduct(), postalProduct);
                postalProduct.setIsPostalMarketOnly(item.getProduct().isPostalMarketOnly()?"1":"0");
                postalProduct.setTaxId(tax.getId());
                postalProduct.setCreatedTime(new Date());
                postalProduct.setUpdatedTime(new Date());
                postalProduct.setModified(DateKit.parseRussiatime(item.getProduct().getModified()));
                postalProductList.add(postalProduct);
            }
            postalProductService.saveOrUpdateBatch(postalProductList);

            //更新所有device的taxIsUpdate 全都改成0
            deviceService.updateLastestTaxVersionUpdateStatuts();

            //redis保存版本内容  https://www.sojson.com/可以恢复
            String jsonStr = JSON.toJSONString(taxVersionDTO);
            redisService.set(taxVersionDTO.getVersion(), jsonStr);
            try {
                //保险起见保存到文件中
                FileUtil.writeBytes(jsonStr.getBytes("UTF-8"), savePath);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
            log.info("保存tax结束，耗时：{}", (System.currentTimeMillis() - t1));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RcsApiException(RcsApiErrorEnum.SaveTaxVersionError);
        }
    }

    @Override
    public Tax getLastestTax() {
        LambdaQueryWrapper<Tax> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tax::getId).last("limit 1");
        return this.getOne(queryWrapper);
    }
}
