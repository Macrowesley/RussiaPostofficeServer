package cc.mrbird.febs.device.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
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
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.entity.FmStatusLog;
import cc.mrbird.febs.rcs.service.IFmStatusLogService;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    IUserDeviceService userDeviceService;
    @Autowired
    IUserService userService;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IFmStatusLogService statusLogService;

    @Autowired
    ChannelMapperManager channelMapperManager;

    @Override
    public IPage<Device> findDevices(QueryRequest request, Device device) {
        if (device == null) {
            device = new Device();
        }
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();

//        Page<Device> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Device> page = new Page<>();
        SortUtil.handlePageSort(request, page, "device_id", FebsConstant.ORDER_DESC, false);


        User curUser = FebsUtil.getCurrentUser();
        String roleId = curUser.getRoleId();

        IPage<Device> deviceIPage = null;
        if (roleId.equals(RoleType.systemManager)) {
            if (StringUtils.isNotBlank(device.getAcnum())) {
                queryWrapper.eq(Device::getAcnum, device.getAcnum());
            }

            if (StringUtils.isNotBlank(device.getNickname())) {
                queryWrapper.eq(Device::getNickname, device.getNickname());
            }

            if (device.getCurFmStatus() == null) {
                queryWrapper.gt(Device::getCurFmStatus, -1);
            } else {
                queryWrapper.eq(Device::getCurFmStatus, device.getCurFmStatus());
            }

            deviceIPage = this.page(page, queryWrapper);
        } else {
            deviceIPage = this.baseMapper.selectListByUserId(page, curUser.getUserId(), device);
        }

        deviceIPage.getRecords().stream().forEach(item -> {
            item.setIsOnline(channelMapperManager.containsKeyAcnum(item.getAcnum()) ? 1 : 0);
        });

        return deviceIPage;
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
        device.setCurFmStatus(Integer.valueOf(updateDeviceDTO.getDeviceStatus()));

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
     * 添加设备的时候，绑定设备到机构管理员
     *
     * @param device
     * @param acnumList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceList(AddDeviceDTO addDeviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(addDeviceDTO, device);
        device.setCurFmStatus(Integer.valueOf(addDeviceDTO.getDeviceStatus()));
        List<String> list = Arrays.asList(addDeviceDTO.getAcnumList().trim().toUpperCase().split(","));
//        long curUserId = FebsUtil.getCurrentUser().getUserId();
        List<UserDevice> userDeviceList = new ArrayList<>();
        list.stream().forEach(acnum -> {
                    device.setAcnum(acnum);
                    device.setNickname(acnum);
                    device.setSecretKey(AESUtils.generateUUID16Len());
                    device.setCreatedTime(new Date());
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
     * todo 从FM中的信息中添加设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMachineInfo(Device dbDevice, DeviceDTO deviceDTO) {
        BeanUtils.copyProperties(deviceDTO, dbDevice);
        dbDevice.setFrankMachineId(deviceDTO.getId());
        dbDevice.setCurFmStatus(FMStatusEnum.ADD_MACHINE_INFO.getCode());
        dbDevice.setFutureFmStatus(FMStatusEnum.ADD_MACHINE_INFO.getCode());
        dbDevice.setFlow(FlowEnum.FlowEnd.getCode());
        dbDevice.setFlowDetail(FlowDetailEnum.DEFAULT.getCode());
        dbDevice.setFmEvent(1);
        dbDevice.setUpdatedTime(new Date());

        this.saveOrUpdate(dbDevice);

        log.info("机器通过协议更新信息device到数据库");
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
     * 机构管理员绑定设备到指定工作人员
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

    /**
     * 改变机器状态：开始
     *
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeStatusBegin(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO) throws RuntimeException {

        Device dbDevice = getDeviceByFrankMachineId(frankMachineId);

        if (dbDevice.getFlow() == FlowEnum.FlowIng.getCode()) {
            throw new RcsApiException(RcsApiErrorEnum.WaitStatusChangeFinish);
        }
        try {
            dbDevice.setFrankMachineId(frankMachineId);
            dbDevice.setFutureFmStatus(changeStatusRequestDTO.getStatus().getCode());
            dbDevice.setPostOffice(changeStatusRequestDTO.getPostOffice());
            //开始修改的话，把流程状态改成进行中
            dbDevice.setFlow(FlowEnum.FlowIng.getCode());
            dbDevice.setUpdatedTime(new Date());

            LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Device::getFrankMachineId, dbDevice.getFrankMachineId());
            this.update(dbDevice, wrapper);

            //保存状态改变记录
            FmStatusLog fmStatusLog = new FmStatusLog();
            BeanUtils.copyProperties(dbDevice, fmStatusLog);
            //log.info("\ndevice={},\nfmStatusLog={}",dbDevice.toString(), fmStatusLog.toString());
            fmStatusLog.setChangeFrom(ChangeFromEnum.Russia.getCode());
            fmStatusLog.setInterfaceName(InterfaceNameEnum.CHANGE_STATUS.getCode());
            fmStatusLog.setUpdatedTime(new Date());
            statusLogService.saveOrUpdate(fmStatusLog);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RcsApiException(RcsApiErrorEnum.ChangeStatusError);
        }
    }

    /**
     * 改变机器状态：结束
     *
     * @param deviceDTO
     * @param flowRes
     */
    @Override
    public void changeStatusEnd(DeviceDTO deviceDTO, FlowDetailEnum curFlowDetail, boolean isMachineActive) {
        /*DeviceDTO device = new DeviceDTO();
        device.setId(frankMachineId);
        device.setStatus(status);
        device.setPostOffice(postOffice);
        device.setTaxVersion(taxVersion);
        device.setEventEnum(event);*/

        //fm想要改变的状态
        int fmChangeStatus = deviceDTO.getStatus().getCode();

        Device device = new Device();
        switch (curFlowDetail) {
            case StatusChangeEndSuccess:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                device.setCurFmStatus(fmChangeStatus);
                break;
            case StatusChangeEndFailUnKnow:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                break;
            case StatusChangeError4xx:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                break;
        }

        device.setFutureFmStatus(fmChangeStatus);
        device.setFlowDetail(curFlowDetail.getCode());
        device.setUpdatedTime(new Date());
        device.setFrankMachineId(deviceDTO.getId());

        //更新数据库
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, device.getFrankMachineId());
        this.update(device, wrapper);

        //保存状态记录
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(isMachineActive ? ChangeFromEnum.Machine.getCode() : ChangeFromEnum.Russia.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.CHANGE_STATUS.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        fmStatusLog.setCreatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

    /**
     * 改变auth状态
     *
     * @param id
     * @param curFlowDetail
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeAuthStatus(Device device, String frankMachineId, FlowDetailEnum curFlowDetail) {
//        Device device = new Device();
        device.setFrankMachineId(frankMachineId);

        switch (curFlowDetail) {
            case AuthEndSuccess:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                device.setCurFmStatus(FMStatusEnum.ENABLED.getCode());
                break;
            case AuthEndFail:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                break;
            default:
                device.setFlow(FlowEnum.FlowIng.getCode());
                break;
        }

        device.setFutureFmStatus(FMStatusEnum.ENABLED.getCode());
        device.setFlowDetail(curFlowDetail.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //保存状态
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(ChangeFromEnum.Machine.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.Auth.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeUnauthStatus(Device device, String frankMachineId, FlowDetailEnum curFlowDetail) {
        device.setFrankMachineId(frankMachineId);

        switch (curFlowDetail) {
            case UnauthEndSuccess:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                device.setCurFmStatus(FMStatusEnum.UNAUTHORIZED.getCode());
                break;
            case UnAuthEndFail:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                break;
            default:
                device.setFlow(FlowEnum.FlowIng.getCode());
                break;
        }

        device.setFutureFmStatus(FMStatusEnum.UNAUTHORIZED.getCode());
        device.setFlowDetail(curFlowDetail.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //保存状态
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(ChangeFromEnum.Machine.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.UnAuth.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeLostStatus(Device device, String frankMachineId, FlowDetailEnum curFlowDetail) {
        device.setFrankMachineId(frankMachineId);

        switch (curFlowDetail) {
            case LostEndSuccess:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                device.setCurFmStatus(FMStatusEnum.LOST.getCode());
                break;
            case LostEndFail:
                device.setFlow(FlowEnum.FlowEnd.getCode());
                break;
            case LostError:
                device.setFlow(FlowEnum.FlowIng.getCode());
                break;
        }

        device.setFutureFmStatus(FMStatusEnum.LOST.getCode());
        device.setFlowDetail(curFlowDetail.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //保存状态
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(ChangeFromEnum.Machine.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.Lost.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensStatus(Device dbDevice, FlowDetailEnum curFlowDetail) {
        String frankMachineId = dbDevice.getFrankMachineId();

        //当没有异常发生的时候，则等着进入下一环节，
        //出现异常：闭环
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess) {
            dbDevice.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            dbDevice.setFlow(FlowEnum.FlowEnd.getCode());
        }

        //更新device的flow和flowDetail
        dbDevice.setFlowDetail(curFlowDetail.getCode());
        dbDevice.setUpdatedTime(new Date());
        this.update(dbDevice, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));
    }

    /**
     * 通过frankMachineId得到acnum
     *
     * @param frankMachineId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public String getAcnumByFMId(String frankMachineId) {
        Device device = getDeviceByFrankMachineId(frankMachineId);
        return device.getAcnum();
    }

    /**
     * 通过frankMachineId判断是否存在
     *
     * @param frankMachineId
     * @return
     */
    @Override
    public boolean checkExistByFmId(String frankMachineId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, frankMachineId);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    //todo 需要加缓存
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public Device getDeviceByFrankMachineId(String frankMachineId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, frankMachineId);
        Device device = this.getOne(wrapper);

        if (device == null) {
            log.error("Unknown FM Id " + frankMachineId);
            throw new RcsApiException(RcsApiErrorEnum.UnknownFMId);
        }
        return device;
    }

    //todo 需要加缓存
    @Override
    public FlowDetailEnum getFlowDetail(String frankMachineId) {
        return FlowDetailEnum.getByCode(getDeviceByFrankMachineId(frankMachineId).getFlowDetail());
    }

    /**
     * 更新所有device的taxIsUpdate 全都改成0
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void updateLastestTaxVersionUpdateStatuts() {
        Device device = new Device();
        device.setTaxIsUpdate(TaxUpdateEnum.NOT_UPDATE.getCode());
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        //查找所有机器
        wrapper.gt(Device::getTaxIsUpdate, -1);
        this.update(device, wrapper);
    }

    /**
     * 更新device的taxIsUpdate TaxVersion信息和其他信息
     *
     * @param device
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void updateDeviceTaxVersionStatus(Device device) {


        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, device.getFrankMachineId());

        device.setTaxIsUpdate(TaxUpdateEnum.UPDATE.getCode());
        this.update(device, wrapper);

    }
}
