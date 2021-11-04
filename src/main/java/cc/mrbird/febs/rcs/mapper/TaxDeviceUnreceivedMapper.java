package cc.mrbird.febs.rcs.mapper;

import cc.mrbird.febs.rcs.entity.TaxDeviceUnreceived;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 税率表 Mapper
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
public interface TaxDeviceUnreceivedMapper extends BaseMapper<TaxDeviceUnreceived> {

    void saveOneUnique(@Param("bean") TaxDeviceUnreceived bean);

    void saveUniqueBatch(List<TaxDeviceUnreceived> list);
}
