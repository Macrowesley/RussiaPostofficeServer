package cc.mrbird.febs.device.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.mapper.DeviceMapper;
import cc.mrbird.febs.device.service.IDeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 设备表 Service实现
 *
 *
 * @date 2020-05-27 14:56:25
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    public IPage<Device> findDevices(QueryRequest request, Device device) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Device> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Device> findDevices(Device device) {
	    LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDevice(Device device) {
        this.save(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(Device device) {
        this.saveOrUpdate(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Device device) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
