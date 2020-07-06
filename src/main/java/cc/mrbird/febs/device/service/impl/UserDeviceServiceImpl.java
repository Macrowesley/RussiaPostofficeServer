package cc.mrbird.febs.device.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.entity.UserDevice;
import cc.mrbird.febs.device.mapper.UserDeviceMapper;
import cc.mrbird.febs.device.service.IUserDeviceService;
import cc.mrbird.febs.system.entity.User;
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
 * 用户设备关联表 Service实现
 *
 * @author mrbird
 * @date 2020-05-29 14:38:03
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {

    private final UserDeviceMapper userDeviceMapper;

    @Override
    public IPage<UserDevice> findUserDevices(QueryRequest request, UserDevice userDevice) {
        LambdaQueryWrapper<UserDevice> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<UserDevice> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<UserDevice> findUserDevices(UserDevice userDevice) {
	    LambdaQueryWrapper<UserDevice> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public UserDevice findOneUserDevice(UserDevice userDevice) {
        LambdaQueryWrapper<UserDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDevice::getDeviceId, userDevice.getDeviceId());
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserDevice(UserDevice userDevice) {
        this.save(userDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserDevice(UserDevice userDevice) {
        this.saveOrUpdate(userDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserDevice(UserDevice userDevice) {
        LambdaQueryWrapper<UserDevice> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
        wrapper.eq(UserDevice::getUserId, userDevice.getUserId());
	    this.remove(wrapper);
	}
}
