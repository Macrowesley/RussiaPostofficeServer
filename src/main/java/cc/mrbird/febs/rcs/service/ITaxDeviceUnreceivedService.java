package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.entity.TaxDeviceUnreceived;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 税率表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
public interface ITaxDeviceUnreceivedService extends IService<TaxDeviceUnreceived> {

    void saveOneUnique(TaxDeviceUnreceived taxDeviceUnreceived);
    void saveUniqueBatch(List<TaxDeviceUnreceived> list);
    void delete(String frankMachineId, String taxVersion);
}
