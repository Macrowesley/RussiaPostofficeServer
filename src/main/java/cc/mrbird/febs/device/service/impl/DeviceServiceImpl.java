package cc.mrbird.febs.device.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.device.dto.AddDeviceDTO;
import cc.mrbird.febs.device.dto.UpdateDeviceDTO;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.entity.UserDevice;
import cc.mrbird.febs.device.mapper.DeviceMapper;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.device.service.IUserDeviceService;
import cc.mrbird.febs.device.vo.UserDeviceVO;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.entity.UserRole;
import cc.mrbird.febs.system.service.IUserRoleService;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    private final IUserDeviceService userDeviceService;
    private final IUserService userService;
    private final IUserRoleService userRoleService;

    @Override
    public IPage<Device> findDevices(QueryRequest request, Device device) {
        if (device == null) {
            device = new Device();
        }
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();

        Page<Device> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "device_id", FebsConstant.ORDER_DESC, false);


        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();

        if (roleId.equals(RoleType.systemManager)) {
            if (StringUtils.isNotBlank(device.getAcnum())) {
                queryWrapper.eq(Device::getAcnum, device.getAcnum());
            }

            if (StringUtils.isNotBlank(device.getNickname())) {
                queryWrapper.eq(Device::getNickname, device.getNickname());
            }

            if (StringUtils.isNotBlank(device.getDeviceStatus())) {
                queryWrapper.eq(Device::getDeviceStatus, device.getDeviceStatus());
            }

            return this.page(page, queryWrapper);
        } else {
            return this.baseMapper.selectListByUserId(page, curUser.getUserId(), device);
        }
    }

    @Override
    public List<Device> findAllDeviceListByUserId(Long userId) {
        return baseMapper.selectAllListByUserId(userId);
    }

    /**
     * 根据用户获取设备id数组
     *
     * @param bindUserId
     * @return
     */
    @Override
    public Long[] findDeviceIdArrByUserId(Long bindUserId) {
        List<Device> deviceList = findAllDeviceListByUserId(bindUserId);
        Long[] arr = new Long[deviceList.size()];
        for (int i = 0; i < deviceList.size(); i++) {
            arr[i] = deviceList.get(i).getDeviceId();
        }
        return arr;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDevice(Device device) {
        editMoney(device);
        this.save(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(UpdateDeviceDTO updateDeviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(updateDeviceDTO, device);

        editMoney(device);
        this.saveOrUpdate(device);

        if (FebsUtil.getCurrentUser().getRoleId().equals(RoleType.systemManager)) {
            //更新绑定状态
            UserDevice userDevice = new UserDevice();
            userDevice.setId(updateDeviceDTO.getUserDeviceId());
            userDevice.setDeviceId(device.getDeviceId());
//            userDevice.setUserId(updateDeviceDTO.getBindUserId());
            userDeviceService.updateUserDevice(userDevice);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Device device) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        // 设置删除条件
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
    public void saveDeviceList(AddDeviceDTO addDeviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(addDeviceDTO, device);
        List<String> list = Arrays.asList(addDeviceDTO.getAcnumList().trim().toUpperCase().split(","));
//        long curUserId = FebsUtil.getCurrentUser().getUserId();
        List<UserDevice> userDeviceList = new ArrayList<>();
        list.stream().forEach(acnum -> {
                    device.setAcnum(acnum);
                    device.setNickname(acnum);
                    device.setSecretKey(AESUtils.generateUUID16Len());
                    device.setCreateTime(new Date());
                    editMoney(device);

                    this.baseMapper.insert(device);

                    UserDevice userDevice = new UserDevice();
                    userDevice.setDeviceId(device.getDeviceId());
                    userDevice.setUserId(addDeviceDTO.getBindUserId());
                    userDeviceList.add(userDevice);
                }
        );

        userDeviceService.saveBatch(userDeviceList);
    }

    /**
     * 金额保留2位
     *
     * @param device
     */
    private void editMoney(Device device) {
        device.setMaxAmount(MoneyUtils.moneySaveTwoDecimal(device.getMaxAmount()));
        device.setWarnAmount(MoneyUtils.moneySaveTwoDecimal(device.getWarnAmount()));
    }

    /**
     * 根据id查询device
     *
     * @param deviceId
     * @return
     */
    @Override
    public Device findDeviceById(Long deviceId) {
        return baseMapper.selectById(deviceId);
    }


    /**
     * 把设备列表绑定到用户上
     *
     * @param deviceIds
     * @param sendUserId
     */
    @Override
    public void bindDevicesToUser(String deviceIds, Long sendUserId) {
        //删除用户-设备列表中 userId绑定的表头号
        deleteDevicesByBindUserId(sendUserId);
        //把新的关系添加到用户-设备列表中
        List<UserDevice> userDeviceList = new ArrayList<>();
        List<String> deviceIdList = Arrays.asList(deviceIds.split(StringPool.COMMA));
        deviceIdList.forEach(deviceId -> {
            UserDevice userDevice = new UserDevice();
            userDevice.setUserId(sendUserId);
            userDevice.setDeviceId(Long.valueOf(deviceId));
            userDeviceList.add(userDevice);
        });
        userDeviceService.saveBatch(userDeviceList);
    }

    /**
     * 通过绑定的userId删除所有的关系
     *
     * @param userId
     */
    @Override
    public void deleteDevicesByBindUserId(Long userId) {
        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(userId);
        userDeviceService.deleteUserDevice(userDevice);
    }

    /**
     * 获取穿梭框需要的表头号信息
     *
     * @param bindUserId
     * @return
     */
    @Override
    public Map<String, Object> selectDeviceListToTransfer(String bindUserId) {

        List<UserRole> list = userRoleService.queryRoleListByUserId(Long.valueOf(bindUserId));
        if (list.size() == 0) {
            throw new FebsException(MessageUtils.getMessage("device.operation.noList"));
        }
        Long roleType = list.get(0).getRoleId();

        Map<String, Object> data = new HashMap<>(2);
        Long[] res = null;
        switch (String.valueOf(roleType)) {
            case RoleType.auditManager:
                /*data.put("allDevices", findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId()));
                res = findDeviceIdArrByUserId(Long.valueOf(bindUserId));
                data.put("bindDevices", res);
                break;*/
            case RoleType.deviceManage:
                //如果是给设备管理员分配，那么，一个表头号给一个设备管理者分配后，不能再分配给另一个人
                //获取父id绑定的所有表头号 list1
                //获取父id下的所有的设备管理者(传进来的设备管理者除外)id绑定的表头号，去重 list2
                List<Device> parentUserDeviceList = findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId());
                List<Device> subUserDeviceList = selectSubUserDeviceListExcepBindUserIdByRoleAndParent(Long.valueOf(bindUserId), FebsUtil.getCurrentUser().getUserId(), roleType);
                //过滤后的deviceList
                subUserDeviceList.stream().forEach(device -> {
                    parentUserDeviceList.remove(device);
                });

                res = findDeviceIdArrByUserId(Long.valueOf(bindUserId));
                data.put("allDevices", parentUserDeviceList);
                data.put("bindDevices", res);
                break;
            default:
                throw new FebsException(MessageUtils.getMessage("device.operation.noList"));
        }

        /*data.put("allDevices", findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId()));
        res = findDeviceIdArrByUserId(Long.valueOf(bindUserId));
        data.put("bindDevices", res);
*/

        return data;
    }

    /**
     * 获取父id下的某类管理者(传进来的管理者除外)id绑定的表头号
     *
     * @param bindUserId
     * @return
     */
    @Override
    public List<Device> selectSubUserDeviceListExcepBindUserIdByRoleAndParent(Long bindUserId, Long parentUserId, Long roleType) {
        return baseMapper.selectSubUserDeviceListExcepBindUserIdByRoleAndParent(bindUserId, parentUserId, roleType);
    }

    @Override
    public UserDeviceVO findByDeviceIdAndRoleId(Long deviceId, Long roleId) {
        /*UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceId);
        return userDeviceService.findOneUserDevice(userDevice);*/
        return baseMapper.findByDeviceIdAndRoleId(deviceId, roleId);
    }

    @Override
    public Device findDeviceByAcnum(String acnum) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getAcnum, acnum);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
