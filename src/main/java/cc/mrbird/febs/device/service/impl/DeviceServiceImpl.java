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
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.entity.FmStatusLog;
import cc.mrbird.febs.rcs.service.IFmStatusLogService;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.entity.UserRole;
import cc.mrbird.febs.system.service.IUserRoleService;
import cc.mrbird.febs.system.service.IUserService;
import cn.hutool.core.date.DateTime;
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

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ????????? Service??????
 *
 * @date 2020-05-27 14:56:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {
    @Autowired
    MessageUtils messageUtils;

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
     * ????????????????????????id??????
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
            //??????????????????
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
        // ??????????????????
        this.remove(wrapper);
    }

    /**
     * ???????????????????????????
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
     * ??????????????????
     * ??????????????????????????????????????????????????????
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
//                    editMoney(device);

                    this.baseMapper.insert(device);

                    /*UserDevice userDevice = new UserDevice();
                    userDevice.setDeviceId(device.getDeviceId());
                    userDevice.setUserId(addDeviceDTO.getBindUserId());
                    userDeviceList.add(userDevice);*/
                }
        );

//        userDeviceService.saveBatch(userDeviceList);
    }

    /**
     * todo ???FM???????????????????????????
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

        log.info("??????????????????????????????device????????????");
    }

    /**
     * ????????????2???
     *
     * @param device
     */
    private void editMoney(Device device) {
        device.setMaxAmount(MoneyUtils.moneySaveTwoDecimal(device.getMaxAmount()));
        device.setWarnAmount(MoneyUtils.moneySaveTwoDecimal(device.getWarnAmount()));
    }

    /**
     * ??????id??????device
     *
     * @param deviceId
     * @return
     */
    @Override
    public Device findDeviceById(Long deviceId) {
        return baseMapper.selectById(deviceId);
    }


    /**
     * ?????????????????????????????????
     * ????????????????????????????????????????????????
     *
     * @param deviceIds
     * @param sendUserId
     */
    @Override
    public void bindDevicesToUser(String deviceIds, Long sendUserId) {
        //????????????-??????????????? userId??????????????????
        deleteDevicesByBindUserId(sendUserId);
        //??????????????????????????????-???????????????
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
     * ???????????????userId?????????????????????
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
     * ???????????????????????????????????????
     *
     * @param bindUserId
     * @return
     */
    @Override
    public Map<String, Object> selectDeviceListToTransfer(String bindUserId) {

        List<UserRole> list = userRoleService.queryRoleListByUserId(Long.valueOf(bindUserId));
        if (list.size() == 0) {
            throw new FebsException(messageUtils.getMessage("device.operation.noList"));
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
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //?????????id???????????????????????? list1
                //?????????id??????????????????????????????(?????????????????????????????????)id??????????????????????????? list2
                List<Device> parentUserDeviceList = findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId());
                List<Device> subUserDeviceList = selectSubUserDeviceListExcepBindUserIdByRoleAndParent(Long.valueOf(bindUserId), FebsUtil.getCurrentUser().getUserId(), roleType);
                //????????????deviceList
                subUserDeviceList.stream().forEach(device -> {
                    parentUserDeviceList.remove(device);
                });

                res = findDeviceIdArrByUserId(Long.valueOf(bindUserId));
                data.put("allDevices", parentUserDeviceList);
                data.put("bindDevices", res);
                break;
            default:
                throw new FebsException(messageUtils.getMessage("device.operation.noList"));
        }

        /*data.put("allDevices", findAllDeviceListByUserId(FebsUtil.getCurrentUser().getUserId()));
        res = findDeviceIdArrByUserId(Long.valueOf(bindUserId));
        data.put("bindDevices", res);
*/

        return data;
    }

    /**
     * ?????????id?????????????????????(???????????????????????????)id??????????????????
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
     * ????????????????????????????????????
     *
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeStatusBegin(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO) throws RuntimeException {

        Device dbDevice = checkAndGetDeviceByFrankMachineId(frankMachineId);

        if (dbDevice.getFlow() == FlowEnum.FlowIng.getCode()) {
            throw new RcsApiException(RcsApiErrorEnum.WaitStatusChangeFinish);
        }
        try {
            dbDevice.setFrankMachineId(frankMachineId);
            dbDevice.setFutureFmStatus(changeStatusRequestDTO.getStatus().getCode());
            dbDevice.setPostOffice(changeStatusRequestDTO.getPostOffice());
            //???????????????????????????????????????????????????
            dbDevice.setFlow(FlowEnum.FlowIng.getCode());
            dbDevice.setFlowDetail(FlowDetailEnum.StatusChangeBegin.getCode());
            dbDevice.setIsRussia(ChangeFromEnum.Russia.getCode());
            dbDevice.setUpdatedTime(new Date());

            LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Device::getFrankMachineId, dbDevice.getFrankMachineId());
            this.update(dbDevice, wrapper);

            //????????????????????????
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
     * ???????????????????????????
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

        //fm?????????????????????
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
        device.setIsRussia(isMachineActive ? ChangeFromEnum.Machine.getCode() : ChangeFromEnum.Russia.getCode());
        device.setUpdatedTime(new Date());
        device.setFrankMachineId(deviceDTO.getId());

        //???????????????
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, device.getFrankMachineId());
        this.update(device, wrapper);

        //??????????????????
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(isMachineActive ? ChangeFromEnum.Machine.getCode() : ChangeFromEnum.Russia.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.CHANGE_STATUS.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        fmStatusLog.setCreatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

    /**
     * ??????auth??????
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
        device.setIsRussia(ChangeFromEnum.Machine.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //????????????
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
        device.setIsRussia(ChangeFromEnum.Machine.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //????????????
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
            case LostErrorUnknow:
                device.setFlow(FlowEnum.FlowIng.getCode());
                break;
        }

        device.setFutureFmStatus(FMStatusEnum.LOST.getCode());
        device.setFlowDetail(curFlowDetail.getCode());
        device.setIsRussia(ChangeFromEnum.Machine.getCode());
        device.setUpdatedTime(new Date());
        this.update(device, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));

        //????????????
        FmStatusLog fmStatusLog = new FmStatusLog();
        BeanUtils.copyProperties(device, fmStatusLog);
        fmStatusLog.setChangeFrom(ChangeFromEnum.Machine.getCode());
        fmStatusLog.setInterfaceName(InterfaceNameEnum.Lost.getCode());
        fmStatusLog.setUpdatedTime(new Date());
        statusLogService.saveOrUpdate(fmStatusLog);
    }

   /* @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensStatus(Device dbDevice, FlowDetailEnum curFlowDetail) {
        String frankMachineId = dbDevice.getFrankMachineId();

        //???????????????????????????????????????????????????????????????
        //?????????????????????
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess) {
            dbDevice.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            dbDevice.setFlow(FlowEnum.FlowEnd.getCode());
        }

        //??????device???flow???flowDetail
        dbDevice.setFlowDetail(curFlowDetail.getCode());
        dbDevice.setUpdatedTime(new Date());
        this.update(dbDevice, new LambdaQueryWrapper<Device>().eq(Device::getFrankMachineId, frankMachineId));
    }*/

    /**
     * ??????frankMachineId??????acnum
     *
     * @param frankMachineId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public String getAcnumByFMId(String frankMachineId) {
        Device device = checkAndGetDeviceByFrankMachineId(frankMachineId);
        return device.getAcnum();
    }

    /**
     * ??????frankMachineId??????????????????
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

    @Override
    @Transactional(rollbackFor = FmException.class)
    public Device checkAndGetDeviceByFrankMachineId(String frankMachineId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getFrankMachineId, frankMachineId);
        Device device = this.getOne(wrapper);

        if (device == null) {
            log.error("Unknown FM Id " + frankMachineId);
            throw new FmException(FMResultEnum.DeviceNotFind);
        }
        return device;
    }

    //todo ???????????????
    @Override
    public FlowDetailEnum getFlowDetail(String frankMachineId) {
        return FlowDetailEnum.getByCode(checkAndGetDeviceByFrankMachineId(frankMachineId).getFlowDetail());
    }

    /**
     * ????????????device???taxIsUpdate ????????????0
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void updateLastestTaxVersionUpdateStatuts() {
        Device device = new Device();
        device.setTaxIsUpdate(TaxUpdateEnum.NOT_UPDATE.getCode());
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        //??????????????????
        wrapper.gt(Device::getTaxIsUpdate, -1);
        this.update(device, wrapper);
    }

    /**
     * ??????device???taxIsUpdate TaxVersion?????????????????????
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

    /**
     * ????????????1000?????????
     */
    @Override
    public void testInsert() {

        log.info("????????????");
        Boolean b = null;
        //??????1000?????????/??????10000???FMID??????
        String FMHead = "PM";
        //????????????NumberFormat?????????
        NumberFormat nf = NumberFormat.getInstance();
        //????????????????????????
        nf.setGroupingUsed(false);
        //????????????????????????
        nf.setMaximumIntegerDigits(6);
        //????????????????????????
        nf.setMinimumIntegerDigits(6);

        //??????1000?????????
        String acnumHead = "S0";
        //????????????NumberFormat?????????
        NumberFormat nf1 = NumberFormat.getInstance();
        //????????????????????????
        nf1.setGroupingUsed(false);
        //????????????????????????
        nf1.setMaximumIntegerDigits(4);
        //????????????????????????
        nf1.setMinimumIntegerDigits(4);

        ArrayList<Device> deviceArrayList = new ArrayList<>();
        for(Long i = 1L;i<2;i++){
            //for(Long i = 1L;i<1001;i++){
            String frankMachineId = FMHead+nf.format(i);
            System.out.println(frankMachineId);

            String acnum = acnumHead + nf1.format(i);
            System.out.println(acnum);
            //Object order = orderMapper.selectById(i);
            Device device = new Device();
            device.setDeviceId(i);
            device.setAcnum(acnum);
            device.setNickname(acnum);
            device.setWarnAmount("10000.00");
            device.setMaxAmount("100000.00");
            device.setSecretKey("b4b9d97816a46b25");
            device.setValidDays(7);
            device.setUseLock("0");
            device.setLockInfo("");
            device.setCreatedTime(new DateTime());
            device.setCurFmStatus(1);
            device.setFlow(1);
            device.setFlowDetail(21);
            device.setIsRussia(0);
            device.setErrorCode("");
            device.setErrorMessage("");
            device.setFmEvent(1);
            device.setFrankMachineId(frankMachineId);
            device.setPostOffice("131000");
            device.setTaxVersion("11.0.7");
            device.setTaxIsUpdate(1);
            device.setUpdatedTime(new DateTime());
            device.setUserId("");
            deviceArrayList.add(device);
        }
        this.saveBatch(deviceArrayList);

    }
}
