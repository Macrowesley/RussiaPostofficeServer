package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.rcs.entity.TaxDeviceUnreceived;
import cc.mrbird.febs.rcs.mapper.TaxDeviceUnreceivedMapper;
import cc.mrbird.febs.rcs.service.ITaxDeviceUnreceivedService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TaxDeviceUnreceivedServiceImpl extends ServiceImpl<TaxDeviceUnreceivedMapper, TaxDeviceUnreceived> implements ITaxDeviceUnreceivedService {

    @Override
    public void saveOneUnique(TaxDeviceUnreceived taxDeviceUnreceived) {
        this.baseMapper.saveOneUnique(taxDeviceUnreceived);
    }

    @Override
    public void saveUniqueBatch(List<TaxDeviceUnreceived> list) {
        this.baseMapper.saveUniqueBatch(list);
    }

    /**
     * 机器完成tax更新后，删除一条记录
     * @param frankMachineId
     * @param taxVersion
     */
    @Override
    public void delete(String frankMachineId, String taxVersion) {
        LambdaQueryWrapper<TaxDeviceUnreceived> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaxDeviceUnreceived::getFrankMachineId, frankMachineId);
        queryWrapper.eq(TaxDeviceUnreceived::getTaxVersion, taxVersion);
        this.baseMapper.delete(queryWrapper);
    }
}
