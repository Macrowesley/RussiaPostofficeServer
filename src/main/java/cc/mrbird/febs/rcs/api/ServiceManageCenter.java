package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionFMDTO;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.DoubleKit;
import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.service.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 管理中心
 * 主要作用：
 * 1. 机器访问服务器的协议中调用本类的方法，一个协议调用一个方法，
 * 2. 处理访问服务器的返回
 * 3. 处理1,2之间的数据库更新
 */
@Component
@NoArgsConstructor
@Slf4j
public class ServiceManageCenter {
    int waitTime = 1;

    @Autowired
    ServiceInvokeRussia serviceInvokeRussia;

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    IPublicKeyService publicKeyService;

    @Autowired
    IPrintJobService printJobService;

    @Autowired
    IContractService contractService;

    @Autowired
    ITaxService taxService;

    @Autowired
    ICustomerService customerService;

    /**
     * 机器状态改变事件
     * 【FM状态改变协议】
     *  只有在俄罗斯要改变机器状态，通知了服务器，服务器通知机器后，机器状态改变，通知服务器，才用这个方法
     *
     * @param deviceDTO
     */
    public void changeStatusEvent(DeviceDTO deviceDTO, boolean isMachineActive) {

        //判断再次访问这个接口的时候，需要的验证
        /**
         * 正常情况下，访问这个接口，机器的flow为未闭环
         * 所以
         * 如果flow为未闭环 继续往后走
         * 如果flow为闭环了，不继续了
         */
        String frankMachineId = deviceDTO.getId();
        String operationName = "changeStatusEvent";

        Device dbDevice = deviceService.getDeviceByFrankMachineId(deviceDTO.getId());
        int dbCurStatus = dbDevice.getCurFmStatus();
        int dbFurStatus = dbDevice.getFutureFmStatus();

/*
        if (dbDevice.getFlow() != FlowEnum.FlowIng.getCode()) {
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }
*/

        //访问俄罗斯服务器，改变状态
        ApiResponse apiResponse = serviceInvokeRussia.frankMachines(deviceDTO);

        if (!apiResponse.isOK()) {
            if (apiResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                deviceService.changeStatusEnd(deviceDTO,  FlowDetailEnum.StatusChangeEndFailUnKnowError, isMachineActive);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "changeStatusEvent.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                deviceService.changeStatusEnd(deviceDTO,  FlowDetailEnum.StatusChangeError4xxError, isMachineActive);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "changeStatusEvent.isOK() false ");
            }
        }
        //更新数据库
        deviceService.changeStatusEnd(deviceDTO, FlowDetailEnum.StatusChangeEndSuccess, isMachineActive);
        log.info("{} 操作成功",operationName);
    }

    /**
     * 注册机器信息到服务器
     * @param deviceDTO
     * @return
     * @throws Exception
     */
//    @Deprecated
    public void addMachineInfo(String acnum, DeviceDTO deviceDTO) throws Exception {
        Device dbDevice = deviceService.findDeviceByAcnum(acnum);
        if (dbDevice == null){
            throw new FmException(FMResultEnum.DeviceNotFind.getCode(), "auth.isOK() false ");
        }
        deviceService.addMachineInfo(dbDevice, deviceDTO);
    }

    /**
     * 【机器请求授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public void auth(DeviceDTO deviceDTO) throws Exception {
        String operationName = "auth";
        String frankMachineId = deviceDTO.getId();
        log.error("服务器收到了设备{}发送的auth协议", frankMachineId);
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;
        /**
         * 过滤：
         * 1. 闭环的不通过
         * 2. 未闭环，而且要改的状态是如果不是auth，也不通过
         */
        if (isFirstAuth && curFlowDetail == FlowDetailEnum.AuthEndSuccess) {
            log.info("auth 已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(), "auth.isOK() false ");
        }

        if (!isFirstAuth && dbFutureStatus != FMStatusEnum.ENABLED) {
            log.error("auth 未闭环，但是要改的状态不是 FMStatusEnum.ENABLED dbFutureStatus={}， 应该是{}", dbFutureStatus, FMStatusEnum.ENABLED);
            throw new FmException(FMResultEnum.StatusTypeError.getCode(), "auth.isOK() false ");
        }


        /**
         * 接下来都是未闭环的操作
         * 情况1：第一次，全部走一遍
         *      is First Auth
         * 情况2：第一次碰到了问题，第二次再次走一遍
         *      is not First Auth
         *      - error1
         *          auth publickey
         *      - error2
         *          publickey
         *      - fail
         *          auth publickey
         */

        //访问俄罗斯服务器，请求授权

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.AuthError1 || curFlowDetail == FlowDetailEnum.AuthEndFail) {
            ApiResponse authResponse = serviceInvokeRussia.auth(frankMachineId, deviceDTO);

            if (!authResponse.isOK()) {
                if (authResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeAuthStatus(dbDevice, frankMachineId, FlowDetailEnum.AuthError1);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "auth.isOK() false ");
                } else {
                    //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                    deviceService.changeAuthStatus(dbDevice, frankMachineId, FlowDetailEnum.AuthEndFail);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "auth.isOK() false ");
                }
            }
            log.info("设备{}开始更新auth状态为{}",frankMachineId,FlowDetailEnum.AuthEndSuccess.getCode());
            deviceService.changeAuthStatus(dbDevice, frankMachineId, FlowDetailEnum.AuthEndSuccess);
        }

        //如果授权成功，数据库改状态，机器开机会自动检测
        PublicKey publicKey = publicKeyService.saveOrUpdatePublicKey(frankMachineId);
//        serviceToMachineProtocol.sentPrivateKeyInfo(frankMachineId, publicKey);

        log.info("{} 操作成功",operationName);
    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public void unauth(DeviceDTO deviceDTO) {
        String operationName = "unauth";
        String frankMachineId = deviceDTO.getId();
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;


        if (isFirstAuth && curFlowDetail == FlowDetailEnum.UnauthEndSuccess) {
            log.info("已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }

        if (!isFirstAuth && dbFutureStatus != FMStatusEnum.UNAUTHORIZED) {
            log.error("未闭环，但是要改的状态不对 dbFutureStatus={}， 应该是{}", dbFutureStatus, FMStatusEnum.UNAUTHORIZED);
            throw new FmException(FMResultEnum.StatusTypeError.getCode(),"未闭环，但是要改的状态不对");
        }

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.UnAuthEndFail || curFlowDetail == FlowDetailEnum.UnAuthError) {
            ApiResponse unauthResponse = serviceInvokeRussia.unauth(deviceDTO.getId(), deviceDTO);
            if (!unauthResponse.isOK()) {
                if (unauthResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeUnauthStatus(dbDevice, frankMachineId, FlowDetailEnum.UnAuthError);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "unauth.isOK() false ");
                } else {
                    //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                    deviceService.changeUnauthStatus(dbDevice, frankMachineId, FlowDetailEnum.UnAuthEndFail);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "unauth.isOK() false ");
                }
            }
            deviceService.changeUnauthStatus(dbDevice, frankMachineId, FlowDetailEnum.UnauthEndSuccess);
            log.info("{} 操作成功",operationName);
        }
    }


    /**
     * 【机器请求lost协议】调用本方法
     *
     * @param deviceDTO
     */
    public void lost(DeviceDTO deviceDTO) {
        String operationName = "lost";
        String frankMachineId = deviceDTO.getId();
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbCurFmStatus = FMStatusEnum.getByCode(dbDevice.getCurFmStatus());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;

        if (dbCurFmStatus == FMStatusEnum.LOST) {
            log.info("已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }

        if (!isFirstAuth && dbFutureStatus != FMStatusEnum.LOST) {
            log.error("未闭环，但是要改的状态不对 dbFutureStatus={}， 应该是{}", dbFutureStatus, FMStatusEnum.LOST);
            throw new FmException(FMResultEnum.StatusTypeError.getCode(),"未闭环，但是要改的状态不对");
        }

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.LostError || curFlowDetail == FlowDetailEnum.LostEndFail) {
            ApiResponse unauthResponse = serviceInvokeRussia.lost(deviceDTO.getId(), deviceDTO);
            if (!unauthResponse.isOK()) {
                if (unauthResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeLostStatus(dbDevice, frankMachineId, FlowDetailEnum.LostError);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "lost.isOK() false ");
                } else {
                    //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                    deviceService.changeLostStatus(dbDevice, frankMachineId, FlowDetailEnum.LostEndFail);
                    log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                    throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "lost.isOK() false ");
                }
            }
            deviceService.changeLostStatus(dbDevice, frankMachineId, FlowDetailEnum.LostEndSuccess);
            log.info("{} 操作成功",operationName);
        }
    }


    /**
     * 机器开机发送tax版本信息，需要更新的时候，触发这个方法
     * @param dbDevice
     */
    public void rateTableUpdateEvent(Device dbDevice) {
        String operationName = "rateTableUpdateEvent";
        String frankMachineId = dbDevice.getFrankMachineId();

        //访问俄罗斯服务器，改变状态
//        ApiResponse changeTaxVersionResponse = serviceInvokeManager.frankMachines(deviceDTO);

        /*
            {
              "taxVersion": "RP.202001-1",
              "status": true,
              "rcsVersions": [
                "A0042015A",
                "B0042015A",
                "C0042015A",
                "D0042015A",
                "E0042015A"
              ]
            }
         */
        RateTableFeedbackDTO rateTableFeedbackDTO = new RateTableFeedbackDTO();
        rateTableFeedbackDTO.setTaxVersion(dbDevice.getTaxVersion());
        rateTableFeedbackDTO.setStatus(true);
        rateTableFeedbackDTO.setRcsVersions(new String[]{"A0042015A","B0042015A","C0042015A","D0042015A","E0042015A"});
        rateTableFeedbackDTO.setTimestamp(DateKit.createRussiatime(new Date()));
        ApiResponse changeTaxVersionResponse = serviceInvokeRussia.rateTables(rateTableFeedbackDTO);

        if (!changeTaxVersionResponse.isOK()) {
            if (changeTaxVersionResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "rateTableUpdateEvent.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "rateTableUpdateEvent.isOK() false ");
            }
        }

        //如果发过来的版本和数据库中最新版本信息一致，则更新状态
        deviceService.updateDeviceTaxVersionStatus(dbDevice);
        log.info("{} 操作成功",operationName);
    }

    /**
     * 收到费率表事件
     * 机器更新了taxVersion后，调用这个协议，触发该方法
     *
     * @param deviceDTO
     */
    @Deprecated
    public void rateTableUpdateEvent(DeviceDTO deviceDTO) {
        String operationName = "rateTableUpdateEvent";
        String frankMachineId = deviceDTO.getId();

        Tax tax = taxService.getLastestTax();

        String fmTaxVersion = deviceDTO.getTaxVersion();
        String dbTaxVersion = tax.getVersion();

        if (!fmTaxVersion.equals(dbTaxVersion)) {
            throw new FmException(FMResultEnum.TaxVersionNeedUpdate.getCode(), "发送版本和最新的版本信息不一致，无法更新，fmTaxVersion="+fmTaxVersion+", dbTaxVersion="+ dbTaxVersion);
        }

        //访问俄罗斯服务器，改变状态
//        ApiResponse changeTaxVersionResponse = serviceInvokeManager.frankMachines(deviceDTO);

        /*
            {
              "taxVersion": "RP.202001-1",
              "status": true,
              "rcsVersions": [
                "A0042015A",
                "B0042015A",
                "C0042015A",
                "D0042015A",
                "E0042015A"
              ]
            }
         */
        RateTableFeedbackDTO rateTableFeedbackDTO = new RateTableFeedbackDTO();
        rateTableFeedbackDTO.setTaxVersion(deviceDTO.getTaxVersion());
        rateTableFeedbackDTO.setStatus(true);
        rateTableFeedbackDTO.setRcsVersions(new String[]{"A0042015A","B0042015A","C0042015A","D0042015A","E0042015A"});
        rateTableFeedbackDTO.setTimestamp(DateKit.createRussiatime(new Date()));
        ApiResponse changeTaxVersionResponse = serviceInvokeRussia.rateTables(rateTableFeedbackDTO);

        if (!changeTaxVersionResponse.isOK()) {
            if (changeTaxVersionResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "rateTableUpdateEvent.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "rateTableUpdateEvent.isOK() false ");
            }
        }

        //如果发过来的版本和数据库中最新版本信息一致，则更新状态
        Device device = new Device();
        device.setFrankMachineId(deviceDTO.getId());
        device.setPostOffice(deviceDTO.getPostOffice());
        device.setTaxVersion(deviceDTO.getTaxVersion());
        deviceService.updateDeviceTaxVersionStatus(device);
        log.info("{} 操作成功",operationName);
    }

    /**
     * 【机器请求foreseens协议】调用本方法
     * 请求打印任务
     */
    public Contract foreseens(ForeseenFMDTO foreseenFMDTO) {

        String operationName = "foreseens";
        String frankMachineId = foreseenFMDTO.getFrankMachineId();
        log.info("foreseens 开始 {}, frankMachineId={}", operationName, frankMachineId);

        //判断机器状态是否正常
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);
        Integer dbCurFmStatus = dbDevice.getCurFmStatus();
        FMStatusEnum dbFMStatus = FMStatusEnum.getByCode(dbCurFmStatus);
        if (dbCurFmStatus == FMStatusEnum.UNAUTHORIZED.getCode() || dbCurFmStatus == FMStatusEnum.LOST.getCode()) {
            throw new FmException(FMResultEnum.StatusNotValid.getCode(), "foreseens 机器状态不正常，当前状态为：" + dbFMStatus.getStatus());
        }

        //判断机器税率表是否更新
        Tax tax = taxService.getLastestTax();
        String fmTaxVersion = foreseenFMDTO.getTaxVersion();
        String dbTaxVersion = tax.getVersion();

        if (!fmTaxVersion.equals(dbTaxVersion)) {
            log.error("发送版本和最新的版本信息不一致，无法更新，fmTaxVersion={}, dbTaxVersion={} ",fmTaxVersion, dbTaxVersion);
            throw new FmException(FMResultEnum.TaxVersionNeedUpdate.getCode(), "发送版本和最新的版本信息不一致，无法更新，fmTaxVersion="+fmTaxVersion+", dbTaxVersion="+ dbTaxVersion);
        }

        //判断publickey是否更新
        if (!publicKeyService.checkFmIsUpdate(dbDevice.getFrankMachineId())){
            log.error("机器{}需要发送公钥和私钥给服务器 ", frankMachineId);
            throw new FmException(FMResultEnum.PrivateKeyNeedUpdate.getCode(), "机器" + frankMachineId + "需要发送公钥和私钥给服务器");
        }

        log.info("foreseenFMDTO.getContractCode() = {}", foreseenFMDTO.getContractCode());
        Contract dbContract = contractService.getByConractCode(foreseenFMDTO.getContractCode());
        Double dbCurrent = dbContract.getCurrent();
        Double dbConsolidate = dbContract.getConsolidate();
        Integer dbEnable = dbContract.getEnable();

        //判断合同状态是否可用
        if (dbEnable == ContractEnableEnum.UNENABLE.getCode()) {
            throw new FmException("foreseens 订单状态不可用，当前订单的状态为：" + dbEnable);
        }

        //判断合同金额是否够用
        //todo 用哪个判断：dbCurrent 还是 dbConsolidate  申请的时候，用哪个来管钱？
        double fmTotalAmount = MoneyUtils.changeF2Y(foreseenFMDTO.getTotalAmmount());
        if (!DoubleKit.isV1BiggerThanV2(dbCurrent, fmTotalAmount) || !DoubleKit.isV1BiggerThanV2(dbConsolidate, fmTotalAmount) || Long.valueOf(foreseenFMDTO.getTotalAmmount()) == 0) {
            throw new FmException(FMResultEnum.MoneyTooBig.getCode(), "foreseens 订单金额 fmTotalAmount为" + fmTotalAmount + "，数据库中合同dbCurrent为：" + dbCurrent + "，dbConsolidate为：" + dbConsolidate);
        }

        //fm信息转ForeseenDTO
        ForeseenDTO foreseenDTO = new ForeseenDTO();
        BeanUtils.copyProperties(foreseenFMDTO, foreseenDTO);
        foreseenDTO.setTotalAmmount(fmTotalAmount);

        //处理UserId
        String userId = getUserIdByContractCode(foreseenDTO.getContractCode());
        foreseenDTO.setUserId(userId);

        ApiResponse foreseensResponse = serviceInvokeRussia.foreseens(foreseenDTO);
        if (!foreseensResponse.isOK()) {
            if (foreseensResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobEndFailForeseensUnKnowError, null);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "foreseensResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                //todo 考虑返回的balance怎么用
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobEndFailForeseens4xxError, null);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "foreseensResponse.isOK() false ");
            }
        }
        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) foreseensResponse.getObject();
        log.info("foreseens 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);

        //正常接收俄罗斯返回，更新数据库
        if (!foreseenDTO.getContractCode().equals(balanceDTO.getContractCode())) {
            throw new FmException(FMResultEnum.contractCodeAbnormal.getCode(), "ContractCode应该是" + foreseenDTO.getContractCode() + "，但是俄罗斯返回的是：" + balanceDTO.getContractCode());
        }
        printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobingForeseensSuccess,balanceDTO);
        log.info("foreseens结束 {}, frankMachineId={}", operationName, frankMachineId);
        return dbContract;
        //下面没有了，会自动返回结果给机器，然后机器选择：取消打印或者开始打印，打印结束后同步金额
    }

    /**
     * 【机器请求transactions协议】调用本方法
     * 交易
     */
    public Contract transactions(TransactionFMDTO transactionFMDTO) {
        String operationName = "transactions";
        String frankMachineId = transactionFMDTO.getFrankMachineId();
        String foreseenId = transactionFMDTO.getForeseenId();
        log.info("transactions 开始 {}, frankMachineId={}", operationName, frankMachineId);

        /**
         判断订单状态是否符合条件：
         只有以下2种情况才能执行transaction： JobingForeseensSuccess 或者 JobErrorTransactionUnKnow
         */
        PrintJob dbPrintJob = printJobService.getByForeseenId(foreseenId);
        if (dbPrintJob == null){
            throw new FmException("dbPrintJob == null foreseenId="+foreseenId);
        }
        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());

        if (curFlowDetail != FlowDetailEnum.JobingForeseensSuccess &&
                curFlowDetail != FlowDetailEnum.JobErrorTransactionUnKnow) {
            throw new FmException("transactions 订单进度不符合条件，frankMachineId = " + frankMachineId + ", foreseenId = " + foreseenId + ", 当前进度为：" + curFlowDetail.getMsg());
        }

        //判断合同状态是否可用
        Contract dbContract = contractService.getByConractCode(transactionFMDTO.getContractCode());
        Integer dbEnable = dbContract.getEnable();
        if (dbEnable == ContractEnableEnum.UNENABLE.getCode()) {
            throw new FmException("transactions 订单状态不可用，当前订单的状态为：" + dbEnable);
        }

        //判断合同金额是否够用
        double fmAmount = MoneyUtils.changeF2Y(transactionFMDTO.getAmount());
        double fmCreditVal = MoneyUtils.changeF2Y(transactionFMDTO.getCreditVal());

        //todo 用哪个判断：dbCurrent 还是 dbConsolidate
        Double dbCurrent = dbContract.getCurrent();
        Double dbConsolidate = dbContract.getConsolidate();
        if (!DoubleKit.isV1BiggerThanV2(dbCurrent, fmAmount)) {
            throw new FmException(FMResultEnum.MoneyTooBig.getCode(),  "transactions 订单金额 fmAmount为" + fmAmount + "，数据库中合同dbCurrent为：" + dbCurrent + "，dbConsolidate为：" + dbConsolidate);
        }

        //数据转换
        TransactionDTO transactionDTO = new TransactionDTO();
        BeanUtils.copyProperties(transactionFMDTO, transactionDTO);
        //消耗的分钟
        int constMinuteTime = Integer.valueOf(transactionFMDTO.getCostTime());

       /* transactionDTO.setStartDateTime(DateKit.formatDate(new Date()));
        transactionDTO.setStopDateTime(DateKit.offsetMinuteToDateTime(constMinuteTime));*/
        transactionDTO.setAmount(fmAmount);
        transactionDTO.setCreditVal(fmCreditVal);

        //处理UserId
        String userId = getUserIdByContractCode(transactionDTO.getContractCode());
        transactionDTO.setUserId(userId);

        ApiResponse transactionsResponse = serviceInvokeRussia.transactions(transactionDTO);
        if (!transactionsResponse.isOK()) {
            if (transactionsResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                //todo 要考虑到这种情况，机器打印结束后，俄罗斯有问题，机器再次发送的情况
                printJobService.changeTransactionStatus(dbPrintJob, dbContract, transactionDTO, FlowDetailEnum.JobErrorTransactionUnKnow,null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "transactionsResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                printJobService.changeTransactionStatus(dbPrintJob,  dbContract, transactionDTO, FlowDetailEnum.JobEndFailTransaction4xxError, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "transactionsResponse.isOK() false ");
            }
        }
        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) transactionsResponse.getObject();
        log.info("transactions 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);
        //正常接收俄罗斯返回，更新数据库
        if (!transactionDTO.getContractCode().equals(balanceDTO.getContractCode())) {
            throw new FmException(FMResultEnum.contractCodeAbnormal.getCode(), "ContractCode应该是" + transactionDTO.getContractCode() + "，但是俄罗斯返回的是：" + balanceDTO.getContractCode());
        }

        Contract curContract = printJobService.changeTransactionStatus(dbPrintJob, dbContract, transactionDTO, FlowDetailEnum.JobEndSuccess, balanceDTO);

        log.info("transactions结束 {}, frankMachineId={} curContract = {}", operationName, frankMachineId, curContract.toString());
        return curContract;
    }

    /**
     * 【机器请求cancelJob协议】调用本方法
     * 取消任务
     */
    public Contract cancelJob(CancelJobFMDTO cancelJobFMDTO) {
        String operationName = "cancelJob";
        String frankMachineId = cancelJobFMDTO.getFrankMachineId();
        String foreseenId = cancelJobFMDTO.getForeseenId();
        String cancelMessage = CancelMsgEnum.getByCode(cancelJobFMDTO.getCancelMsgCode()).getMsg();

        log.info("开始 {}, frankMachineId={}", operationName, frankMachineId);

        /**
         判断订单状态是否符合条件：
         只有以下2种情况才能取消订单： JobingForeseensSuccess 或者 JobErrorForeseensCancelUnKnowError
         */
        PrintJob dbPrintJob = printJobService.getByForeseenId(foreseenId);
        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());

        if (dbFlow == FlowEnum.FlowEnd){
            throw new FmException("cancelJob 订单已经闭环，不能操作了");
        }

        if (curFlowDetail != FlowDetailEnum.JobingForeseensSuccess &&
                curFlowDetail != FlowDetailEnum.JobErrorForeseensCancelUnKnowError) {
            throw new FmException("cancelJob 订单进度不符合条件，frankMachineId = " + frankMachineId + ", foreseenId = " + foreseenId + ", 当前进度为：" + curFlowDetail.getMsg());
        }

        //判断合同状态是否可用
        Contract dbContract = contractService.getByConractCode(dbPrintJob.getContractCode());
        Integer dbEnable = dbContract.getEnable();
        if (dbEnable == ContractEnableEnum.UNENABLE.getCode()) {
            throw new FmException("订单状态不可用，当前订单的状态为：" + dbEnable);
        }


        //给俄罗斯发消息
        ApiResponse cancelResponse = serviceInvokeRussia.cancel(foreseenId,  cancelJobFMDTO.getContractCode(), new ForeseenCancel(cancelMessage));
        if (!cancelResponse.isOK()) {
            if (cancelResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobErrorForeseensCancelUnKnowError);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(),"cancelResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobEndFailForeseensCancel4xxError);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(),"cancelResponse.isOK() false ");
            }
        }
        printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobEndFailForeseensCancelSuccess);

        log.info("结束 {}, frankMachineId={}", operationName, frankMachineId);
        return dbContract;
    }

    /**
     * 获取客户id
     * @param contractCode
     * @return
     */
    private String getUserIdByContractCode(String contractCode) {
        return customerService.getUserIdByContractCode(contractCode);
    }
}
