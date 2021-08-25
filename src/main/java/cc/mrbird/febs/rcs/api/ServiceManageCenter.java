package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.DoubleKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.service.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    IPostOfficeService postOfficeService;

    @Autowired
    ITaxService taxService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    ITransactionMsgService dmMsgService;

    @Autowired
    CheckUtils checkUtils;


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
                deviceService.changeStatusEnd(deviceDTO,  FlowDetailEnum.StatusChangeEndFailUnKnow, isMachineActive);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "changeStatusEvent.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                deviceService.changeStatusEnd(deviceDTO,  FlowDetailEnum.StatusChangeError4xx, isMachineActive);
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
    public void addMachineInfo(String acnum, DeviceDTO deviceDTO) {
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
    public void auth(DeviceDTO deviceDTO)  {
        String operationName = "auth";
        String frankMachineId = deviceDTO.getId();
        log.error("服务器收到了设备{}发送的auth协议", frankMachineId);
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());
        FMStatusEnum dbCurFmStatus = FMStatusEnum.getByCode(dbDevice.getCurFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;
        /**
         * 过滤：
         * 1. 闭环的不通过
         * 2. 未闭环，而且要改的状态是如果不是auth，也不通过
         */
        //如果设备已经是授权状态了，再点击授权就直接拒绝
        if (dbCurFmStatus == FMStatusEnum.ENABLED) {
            log.info("auth 已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(), "auth.isOK() false ");
        }
        //这个方法就是授权请求，如果是在上一次的授权流程中中断了，那么dbFutureStatus应该是ENABLED，但如果不是，肯定是异常状态
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

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.AuthErrorUnKnow || curFlowDetail == FlowDetailEnum.AuthEndFail) {
            ApiResponse authResponse = serviceInvokeRussia.auth(frankMachineId, deviceDTO);

            if (!authResponse.isOK()) {
                if (authResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeAuthStatus(dbDevice, frankMachineId, FlowDetailEnum.AuthErrorUnKnow);
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

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.UnAuthEndFail || curFlowDetail == FlowDetailEnum.UnAuthErrorUnkonw) {
            ApiResponse unauthResponse = serviceInvokeRussia.unauth(deviceDTO.getId(), deviceDTO);
            if (!unauthResponse.isOK()) {
                if (unauthResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeUnauthStatus(dbDevice, frankMachineId, FlowDetailEnum.UnAuthErrorUnkonw);
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

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.LostErrorUnknow || curFlowDetail == FlowDetailEnum.LostEndFail) {
            ApiResponse unauthResponse = serviceInvokeRussia.lost(deviceDTO.getId(), deviceDTO);
            if (!unauthResponse.isOK()) {
                if (unauthResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeLostStatus(dbDevice, frankMachineId, FlowDetailEnum.LostErrorUnknow);
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
     * 机器开机发送tax版本信息，如果机器刚刚更新到了最新的版本号，触发这个方法
     * @param dbDevice
     */
    public void frankMachinesRateTableUpdateEvent(Device dbDevice) {
        String operationName = "frankMachines rateTableUpdateEvent";
        String frankMachineId = dbDevice.getFrankMachineId();

        /*
            {
                 "id": "PM100500",
                 "dateTime": "2021-01-01T09:00:00.001+03:00",
                 "status": "BLOCKED",
                 "postOffice": "131000",
                 "taxVersion": "22.1.1",
                 "event": "RATE_TABLE_UPDATE",
                 "error": {}
             }
         */
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(frankMachineId);
        deviceDTO.setStatus(FMStatusEnum.getByCode(dbDevice.getCurFmStatus()));
        deviceDTO.setPostOffice(dbDevice.getPostOffice());
        deviceDTO.setTaxVersion(dbDevice.getTaxVersion());
        deviceDTO.setEvent(EventEnum.RATE_TABLE_UPDATE);
        deviceDTO.setDateTime(DateKit.createRussiatime());

        ApiResponse changeTaxVersionResponse = serviceInvokeRussia.frankMachinesRateTableUpdateEvent(deviceDTO);

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
     * 告知俄罗斯税率表加载ok
     * @param dbDevice
     */
    public synchronized boolean rateTables(String taxVersion) {
        String operationName = "rateTables";

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
        rateTableFeedbackDTO.setTaxVersion(taxVersion);
        rateTableFeedbackDTO.setStatus(true);
        rateTableFeedbackDTO.setRcsVersions(taxService.getTaxVersionArr());
        rateTableFeedbackDTO.setTimestamp(DateKit.createRussiatime(new Date()));
        ApiResponse changeTaxVersionResponse = serviceInvokeRussia.rateTables(rateTableFeedbackDTO);

        if (!changeTaxVersionResponse.isOK()) {
            if (changeTaxVersionResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                log.error("服务器发送{}协议给俄罗斯，VisitRussiaTimedOut", operationName);
//                throw new FmException(FMResultEnum..getCode(), "rateTableUpdateEvent.isOK() false ");
            } else {
                log.error("服务器发送{}协议给俄罗斯，RussiaServerRefused", operationName);
//                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "rateTableUpdateEvent.isOK() false ");
            }
            return false;
        }

        //更新tax字段记录是否通知俄罗斯
        taxService.alreadyInformRussia(taxVersion);

        log.info("{} 操作成功",operationName);
        return true;
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
        checkUtils.checkFmEnable(frankMachineId);

        //判断机器税率表是否更新
        Tax tax = taxService.getLastestTax();
        String fmTaxVersion = foreseenFMDTO.getTaxVersion();
        String dbTaxVersion = tax.getVersion();

        if (!fmTaxVersion.equals(dbTaxVersion)) {
            log.error("发送版本和最新的版本信息不一致，无法更新，fmTaxVersion={}, dbTaxVersion={} ",fmTaxVersion, dbTaxVersion);
            throw new FmException(FMResultEnum.TaxVersionNeedUpdate.getCode(), "发送版本和最新的版本信息不一致，无法更新，fmTaxVersion="+fmTaxVersion+", dbTaxVersion="+ dbTaxVersion);
        }

        //判断publickey是否更新
        if (!publicKeyService.checkFmIsUpdate(frankMachineId)){
            log.error("机器{}需要发送公钥和私钥给服务器 ", frankMachineId);
            throw new FmException(FMResultEnum.PrivateKeyNeedUpdate.getCode(), "机器" + frankMachineId + "需要发送公钥和私钥给服务器");
        }

        log.info("foreseenFMDTO.getContractCode() = {}", foreseenFMDTO.getContractCode());
        Contract dbContract = checkUtils.checkContractIsOk(foreseenFMDTO.getContractCode());
        Double dbCurrent = dbContract.getCurrent();
        Double dbConsolidate = dbContract.getConsolidate();

        //判断合同金额是否够用
        //todo 用哪个判断：dbCurrent 还是 dbConsolidate  申请的时候，用哪个来管钱？
        double fmTotalAmount = MoneyUtils.changeF2Y(foreseenFMDTO.getTotalAmmount());
        if (!DoubleKit.isV1BiggerThanV2(dbCurrent, fmTotalAmount) || !DoubleKit.isV1BiggerThanV2(dbConsolidate, fmTotalAmount) || Long.valueOf(foreseenFMDTO.getTotalAmmount()) == 0) {
            throw new FmException(FMResultEnum.MoneyTooBig.getCode(), "foreseens 订单金额 fmTotalAmount为" + fmTotalAmount + "，数据库中合同dbCurrent为：" + dbCurrent + "，dbConsolidate为：" + dbConsolidate);
        }

        //判断postOffice是否存在
        checkUtils.checkPostOfficeExist(foreseenFMDTO.getPostOffice());

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
                printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobEndFailForeseensUnKnow, null);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "foreseensResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                //todo 考虑返回的balance怎么用
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobEndFailForeseens4xx, null);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "foreseensResponse.isOK() false ");
            }
        }
        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) foreseensResponse.getObject();
        log.info("foreseens 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);
        balanceDTO.setContractCode(foreseenDTO.getContractCode());
        //正常接收俄罗斯返回，更新数据库
        /*if (!foreseenDTO.getContractCode().equals(balanceDTO.getContractCode())) {
            throw new FmException(FMResultEnum.contractCodeAbnormal.getCode(), "ContractCode应该是" + foreseenDTO.getContractCode() + "，但是俄罗斯返回的是：" + balanceDTO.getContractCode());
        }*/
        printJobService.changeForeseensStatus(foreseenDTO, FlowDetailEnum.JobingForeseensSuccess,balanceDTO);
        log.info("foreseens结束 {}, frankMachineId={}", operationName, frankMachineId);
        dbContract.setCurrent(balanceDTO.getCurrent());
        dbContract.setConsolidate(balanceDTO.getConsolidate());
        return dbContract;
        //下面没有了，会自动返回结果给机器，然后机器选择：取消打印或者开始打印，打印结束后同步金额
    }

    /**
     * 保存批次信息
     * @param transactionMsgFmDto
     * @return
     */
    public String saveTransactionMsg(TransactionMsgFMDTO transactionMsgFmDto) {
        log.info("解析得到的对象：TransactionFMDTO={}", transactionMsgFmDto.toString());

        //判断msg是否符合规范
        checkUtils.checkTransactionMsg(transactionMsgFmDto.getDmMsg().trim());

        //判断机器状态是否正常
        checkUtils.checkFmEnable(transactionMsgFmDto.getFrankMachineId());

        return dmMsgService.saveMsg(transactionMsgFmDto);
    }

    /**
     * 【机器请求transactions协议】调用本方法
     * 交易
     */
    public Contract transactions(TransactionFMDTO transactionFmDto) {
        //todo 测试修改数据
        transactionFmDto.setPostOffice("131999");

        String operationName = "transactions";
        String frankMachineId = transactionFmDto.getFrankMachineId();
        String foreseenId = transactionFmDto.getForeseenId();
        String transactionId = transactionFmDto.getId();
        log.info("transactions 开始 {}, frankMachineId={}", operationName, frankMachineId);

        //校验Postoffice
        checkUtils.checkPostOfficeExist(transactionFmDto.getPostOffice());

        //校验FM id是否存在
        checkUtils.checkFmIdExist(frankMachineId);

        //校验contract
        Contract dbContract = checkUtils.checkContractIsOk(transactionFmDto.getContractCode());

        //校验dmMessage长度，不需要 dmmsg中已经校验了

        //校验订单状态是否符合条件
        PrintJob dbPrintJob = checkUtils.checkTransactionFlowDetailIsOk(foreseenId,frankMachineId);

        //校验机器状态是否正常
        checkUtils.checkFmEnable(frankMachineId);

        //获取需要发送给俄罗斯的数据
        TransactionDTO transactionDTO = getTransactionDTO(transactionFmDto, transactionId, dbContract);

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
                printJobService.changeTransactionStatus(dbPrintJob,  dbContract, transactionDTO, FlowDetailEnum.JobErrorTransaction4xx, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "transactionsResponse.isOK() false ");
            }
        }
        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) transactionsResponse.getObject();
        balanceDTO.setContractCode(transactionDTO.getContractCode());
        log.info("transactions 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);

        //正常接收俄罗斯返回，更新数据库
        /*if (!transactionDTO.getContractCode().equals(balanceDTO.getContractCode())) {
            throw new FmException(FMResultEnum.contractCodeAbnormal.getCode(), "ContractCode应该是" + transactionDTO.getContractCode() + "，但是俄罗斯返回的是：" + balanceDTO.getContractCode());
        }*/

        Contract curContract = printJobService.changeTransactionStatus(dbPrintJob, dbContract, transactionDTO, FlowDetailEnum.JobEndSuccess, balanceDTO);

        log.info("transactions结束 {}, frankMachineId={} curContract = {}", operationName, frankMachineId, curContract.toString());
        return curContract;
    }

    private TransactionDTO getTransactionDTO(TransactionFMDTO transactionFMDTO, String transactionId, Contract dbContract) {
        //机器不让欠钱，暂时为0
        transactionFMDTO.setCreditVal("0");
        //数据库得到具体的dmMsg信息
        DmMsgDetail dmMsgDetail = dmMsgService.getDmMsgDetailAfterFinishJob(transactionId);
        //实际花费的
        transactionFMDTO.setAmount(dmMsgDetail.getActualAmount());
        //预计花费，应该是从foreseen的amount

        transactionFMDTO.setCount(dmMsgDetail.getActualCount());
        transactionFMDTO.setFranks(dmMsgDetail.getFranks());

        //判断合同金额是否够用
        double fmAmount = MoneyUtils.changeF2Y(transactionFMDTO.getAmount());
        double fmCreditVal = MoneyUtils.changeF2Y(transactionFMDTO.getCreditVal());

        Double dbCurrent = dbContract.getCurrent();
        Double dbConsolidate = dbContract.getConsolidate();
        if (!DoubleKit.isV1BiggerThanV2(dbCurrent, fmAmount)) {
            throw new FmException(FMResultEnum.MoneyTooBig.getCode(),  "transactions 订单金额 fmAmount为" + fmAmount + "，数据库中合同dbCurrent为：" + dbCurrent + "，dbConsolidate为：" + dbConsolidate);
        }

        //数据转换
        TransactionDTO transactionDTO = new TransactionDTO();
        BeanUtils.copyProperties(transactionFMDTO, transactionDTO);

        //开始结束时间
        Transaction dbTransaction = printJobService.getTransactionById(transactionId);
        transactionDTO.setStartDateTime(dbTransaction.getStartDateTime());
        transactionDTO.setStopDateTime(DateKit.createRussiatime(new Date()));

        //处理金额
        transactionDTO.setAmount(fmAmount);
        transactionDTO.setCreditVal(fmCreditVal);
        transactionDTO.setGraphId("");

        //处理UserId
        String userId = getUserIdByContractCode(transactionDTO.getContractCode());
        transactionDTO.setUserId(userId);
        return transactionDTO;
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
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"printJob 订单已经闭环，不能操作了");
        }

        if (curFlowDetail != FlowDetailEnum.JobingForeseensSuccess
                && curFlowDetail != FlowDetailEnum.JobErrorForeseensCancelUnKnow
                && curFlowDetail != FlowDetailEnum.JobErrorForeseensCancel4xx) {
            throw new FmException(FMResultEnum.OrderProcessIsNotRight.getCode(),"cancelJob 订单进度不符合条件，frankMachineId = " + frankMachineId + ", foreseenId = " + foreseenId + ", 当前进度为：" + curFlowDetail.getMsg());
        }

        //判断合同状态是否可用
        Contract dbContract = checkUtils.checkContractIsOk(dbPrintJob.getContractCode());


        //给俄罗斯发消息
        ApiResponse cancelResponse = serviceInvokeRussia.cancel(foreseenId,  cancelJobFMDTO.getContractCode(), new ForeseenCancel(cancelMessage));
        if (!cancelResponse.isOK()) {
            if (cancelResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobErrorForeseensCancelUnKnow, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(),"cancelResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobErrorForeseensCancel4xx, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(),"cancelResponse.isOK() false ");
            }
        }

        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) cancelResponse.getObject();
        log.info("transactions 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);

        printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFMDTO, FlowDetailEnum.JobEndFailForeseensCancelSuccess, balanceDTO);

        dbContract.setCurrent(balanceDTO.getCurrent());
        dbContract.setConsolidate(balanceDTO.getConsolidate());
        log.info("结束 {}, frankMachineId={}", operationName, frankMachineId);
        return dbContract;
    }

    /**
     * 获取客户id
     * @param contractCode
     * @return
     */
    private String getUserIdByContractCode(String contractCode) {
        return customerService.getCustomerByContractCode(contractCode).getId();
    }


}
