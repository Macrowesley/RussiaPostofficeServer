package cc.mrbird.febs.rcs.mapper;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmRespDTO;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算订单产品 Mapper
 *
 * @author mrbird
 * @date 2021-04-17 14:44:55
 */
public interface ForeseenProductMapper extends BaseMapper<ForeseenProduct> {

    /**
     * 根据订单id获取对应的产品列表，包含广告图片信息
     * @param printJobId
     * @return
     */
    List<ForeseenProductFmRespDTO> selectProductAdList(@Param("printJobId") Integer printJobId);
}
