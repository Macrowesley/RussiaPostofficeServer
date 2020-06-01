package cc.mrbird.febs.device.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.entity.UserDevice;
import cc.mrbird.febs.device.mapper.DeviceMapper;
import cc.mrbird.febs.device.mapper.UserDeviceMapper;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.device.service.IUserDeviceService;
import cc.mrbird.febs.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备表 Service实现
 *
 * @date 2020-05-27 14:56:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final DeviceMapper deviceMapper;

    private final IUserDeviceService userDeviceService;

    @Override
    public IPage<Device> findDevices(QueryRequest request, Device device) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();
        /*if (roleId.equals(RoleType.organizationManager)) {
            queryWrapper.eq()
        }*/
        if (StringUtils.isNotBlank(device.getAcnum())){
            queryWrapper.eq(Device::getAcnum, device.getAcnum());
        }

        if (StringUtils.isNotBlank(device.getNickname())){
            queryWrapper.eq(Device::getNickname, device.getNickname());
        }

        if (StringUtils.isNotBlank(device.getDeviceStatus())){
            queryWrapper.eq(Device::getDeviceStatus, device.getDeviceStatus());
        }

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

    /**
     * 获取重复表头号信息
     *
     * @param acnumList
     * @return
     */
    @Override
    public Map<String, Object> getRepetitionInfo(String acnumList) {
        List<String> list = Arrays.asList(acnumList.trim().toUpperCase().split(","));
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Device::getAcnum, list);
        List<Device> resList = baseMapper.selectList(queryWrapper);
        String res = resList.stream().map(device -> device.getAcnum()).collect(Collectors.joining(","));

        Map<String, Object> map = new HashMap<>();
        map.put("isExist", 0);
        map.put("data", res);
        if (res.length() > 3) {
            map.put("isExist", 1);
        }
        log.info(map.toString());
        return map;
    }

    /**
     * 批量添加设备
     *
     * @param device
     * @param acnumList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceList(Device device, String acnumList) {
        List<String> list = Arrays.asList(acnumList.trim().toUpperCase().split(","));
        long curUserId = FebsUtil.getCurrentUser().getUserId();
        List<UserDevice> userDeviceList = new ArrayList<>();
        list.stream().forEach(acnum -> {
                    device.setAcnum(acnum);
                    device.setNickname(acnum);
                    device.setCreateTime(new Date());
                    this.baseMapper.insert(device);

                    UserDevice userDevice = new UserDevice();
                    userDevice.setDeviceId(device.getDeviceId());
                    userDevice.setUserId(curUserId);
                    userDeviceList.add(userDevice);
                }
        );

        userDeviceService.saveBatch(userDeviceList);
    }
}
