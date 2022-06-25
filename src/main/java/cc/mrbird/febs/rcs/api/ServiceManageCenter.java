package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.*;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.DoubleKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
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
    ITaxDeviceUnreceivedService taxDeviceUnreceivedService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    ITransactionMsgService dmMsgService;

    @Autowired
    CheckUtils checkUtils;

    @Autowired
    IContractAddressService contractAddressService;

    @Autowired
    public TempKeyUtils tempKeyUtils;

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

        Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(deviceDTO.getId());
        int dbCurStatus = dbDevice.getCurFmStatus();
        int dbFurStatus = dbDevice.getFutureFmStatus();

/*
        if (dbDevice.getFlow() != FlowEnum.FlowIng.getCode()) {
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }
*/

        //访问俄罗斯服务器，改变状态
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.frankMachines(deviceDTO);

        if (!apiRussiaResponse.isOK()) {
            if (apiRussiaResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
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
        log.info("服务器收到了设备{}发送的auth协议", frankMachineId);
        Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(frankMachineId);

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
        /*if (dbCurFmStatus == FMStatusEnum.ENABLED) {
            log.info("auth 已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(), "auth.isOK() false ");
        }*/
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
            ApiRussiaResponse authResponse = serviceInvokeRussia.auth(frankMachineId, deviceDTO);

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
        noticeMachineUpdateKey(frankMachineId, publicKey);

        log.info("{} 操作成功",operationName);
    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public void unauth(DeviceDTO deviceDTO) {
        //测试数据
//        deviceDTO.setId("PM100501");
        String operationName = "unauth";
        String frankMachineId = deviceDTO.getId();

        Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;


        /*if (isFirstAuth && curFlowDetail == FlowDetailEnum.UnauthEndSuccess) {
            log.info("已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }*/

        if (!isFirstAuth && dbFutureStatus != FMStatusEnum.UNAUTHORIZED) {
            log.error("未闭环，但是要改的状态不对 dbFutureStatus={}， 应该是{}", dbFutureStatus, FMStatusEnum.UNAUTHORIZED);
            throw new FmException(FMResultEnum.StatusTypeError.getCode(),"未闭环，但是要改的状态不对");
        }

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.UnAuthEndFail || curFlowDetail == FlowDetailEnum.UnAuthErrorUnkonw) {
            ApiRussiaResponse unauthResponse = serviceInvokeRussia.unauth(deviceDTO.getId(), deviceDTO);
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
        //测试数据
//        deviceDTO.setId("PM100501");

        String operationName = "lost";
        String frankMachineId = deviceDTO.getId();
        Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbCurFmStatus = FMStatusEnum.getByCode(dbDevice.getCurFmStatus());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;

        /*if (dbCurFmStatus == FMStatusEnum.LOST) {
            log.info("已经闭环，且已经完成了{}操作的，直接返回结果即可", operationName);
            throw new FmException(FMResultEnum.DonotAgain.getCode(),"机器的状态已经修改结束了，请勿操作");
        }*/

        if (!isFirstAuth && dbFutureStatus != FMStatusEnum.LOST) {
            log.error("未闭环，但是要改的状态不对 dbFutureStatus={}， 应该是{}", dbFutureStatus, FMStatusEnum.LOST);
            throw new FmException(FMResultEnum.StatusTypeError.getCode(),"未闭环，但是要改的状态不对");
        }

        if (isFirstAuth || curFlowDetail == FlowDetailEnum.LostErrorUnknow || curFlowDetail == FlowDetailEnum.LostEndFail) {
            ApiRussiaResponse unauthResponse = serviceInvokeRussia.lost(deviceDTO.getId(), deviceDTO);
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
     * 机器收到服务器发送的tax，就触发这个方法
     * @param dbDevice
     */
    public void frankMachinesRateTableUpdateEvent(DeviceDTO deviceDTO) {
        log.info("【机器{}收到了tax版本{}，通知服务器】", deviceDTO.getId(), deviceDTO.getTaxVersion());
        String operationName = "frankMachines rateTableUpdateEvent";
        String frankMachineId = deviceDTO.getId();

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

        // 1.通知俄罗斯tax更新了
        ApiRussiaResponse changeTaxVersionResponse = serviceInvokeRussia.frankMachinesRateTableUpdateEvent(deviceDTO);

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

        //2. 更新device数据库
        Device device = new Device();
        device.setFrankMachineId(frankMachineId);
        device.setTaxVersion(deviceDTO.getTaxVersion());
        device.setUpdatedTime(new Date());
        deviceService.updateDeviceTaxVersionStatus(device);

        //3.  从rcs_tax_device_unreceived中删除记录
        taxDeviceUnreceivedService.delete(frankMachineId, deviceDTO.getTaxVersion());

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
//        rateTableFeedbackDTO.setRcsVersions(taxService.getTaxVersionArr());
        rateTableFeedbackDTO.setRcsVersions(new String[]{taxVersion});
        rateTableFeedbackDTO.setTimestamp(DateKit.createRussiatime(new Date()));
        ApiRussiaResponse changeTaxVersionResponse = serviceInvokeRussia.rateTables(rateTableFeedbackDTO);

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
     * 发送foreseen给俄罗斯
     */
    public byte[] foreseens(ForeseenFmReqDTO foreseenFmReqDTO, PrintJob dbPrintJob, ChannelHandlerContext ctx , PrintProgressInfo productPrintProgress) {
        String version = FebsConstant.FmVersion1;
        String foreseenId = "";
        if (FebsConstant.IS_TEST_NETTY) {
            foreseenId = foreseenFmReqDTO.getId();
        }else{
            foreseenId = AESUtils.createUUID();
            foreseenFmReqDTO.setId(foreseenId);
        }

        //机器日期
        Date machineDate = DateKit.parseDateYmdhms(foreseenFmReqDTO.getMachineDate());

        String operationName = "foreseens";
        String frankMachineId = foreseenFmReqDTO.getFrankMachineId();
        log.info("foreseens 开始 {}, frankMachineId={}", operationName, frankMachineId);

        //判断软件许可证书是否过期
        if(!checkUtils.checkLicense()){
            throw new FmException(FMResultEnum.LicenseExpiry);
        }

        //判断机器状态是否正常
        checkUtils.checkFmEnable(frankMachineId);

        //判断机器税率表是否更新
        checkUtils.checkTaxIsOk(frankMachineId, ctx ,foreseenFmReqDTO.getTaxVersion(), foreseenFmReqDTO.getMachineDate());

        //判断publickey是否更新
        if (!publicKeyService.checkFmIsUpdate(frankMachineId)){
            log.error("机器{}需要发送公钥和私钥给服务器 ", frankMachineId);
            throw new FmException(FMResultEnum.PrivateKeyNeedUpdate.getCode(), "机器" + frankMachineId + "需要发送公钥和私钥给服务器");
        }

        log.info("foreseenFmReqDTO.getContractCode() = {}", foreseenFmReqDTO.getContractCode());
        Contract dbContract = checkUtils.checkContractIsOk(foreseenFmReqDTO.getContractCode());
        Double dbCurrent = dbContract.getCurrent();
        Double dbConsolidate = dbContract.getConsolidate();

        //判断合同金额是否够用
        double fmTotalAmount = MoneyUtils.changeF2Y(foreseenFmReqDTO.getTotalAmmount());
        if (!DoubleKit.isV1BiggerThanV2(dbCurrent, fmTotalAmount) || Long.valueOf(foreseenFmReqDTO.getTotalAmmount()) == 0) {
            throw new FmException(FMResultEnum.MoneyTooBig.getCode(), "foreseens 订单金额 fmTotalAmount为" + fmTotalAmount + "，数据库中合同dbCurrent为：" + dbCurrent + "，dbConsolidate为：" + dbConsolidate);
        }

        //判断postOffice是否存在
        checkUtils.checkPostOfficeExist(foreseenFmReqDTO.getPostOffice());

        //给俄罗斯发送预处理订单信息并返回
        ApiRussiaResponse foreseensResponse = serviceInvokeRussia.foreseens(foreseenFmReqDTO);
        if (!foreseensResponse.isOK()) {
            if (foreseensResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                dbPrintJob = printJobService.changeForeseensStatus(foreseenFmReqDTO, dbPrintJob, FlowDetailEnum.JobingErrorForeseensUnKnow, null);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "foreseensResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                //todo 考虑返回的balance怎么用
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                dbPrintJob = printJobService.changeForeseensStatus(foreseenFmReqDTO, dbPrintJob, FlowDetailEnum.JobEndFailForeseens4xx, null);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "foreseensResponse.isOK() false ");
            }
        }
        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) foreseensResponse.getObject();
        log.info("foreseens 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);
        balanceDTO.setContractCode(foreseenFmReqDTO.getContractCode());

        //正常接收俄罗斯返回，更新数据库
        dbPrintJob = printJobService.changeForeseensStatus(foreseenFmReqDTO, dbPrintJob, FlowDetailEnum.JobingForeseensSuccess,balanceDTO);
        log.info("foreseens结束 {}, frankMachineId={}", operationName, frankMachineId);

        dbContract.setCurrent(balanceDTO.getCurrent());
        dbContract.setConsolidate(balanceDTO.getConsolidate());

        //拼接产品进度
        ForeseenProductFmReqDTO[] fmReqDTOProducts = foreseenFmReqDTO.getProducts();
        ForeseenProductFmRespDTO[] productArr = new ForeseenProductFmRespDTO[fmReqDTOProducts.length];


        for (int i = 0; i < fmReqDTOProducts.length; i++) {
            ForeseenProductFmRespDTO temp = new ForeseenProductFmRespDTO();
            BeanUtils.copyProperties(fmReqDTOProducts[i], temp);
            productArr[i] = temp;
        }

        return buildForeseenResultBytes(dbPrintJob, ctx, foreseenId, dbContract, productPrintProgress);
    }

    /**
     * 拼接返回给机器的字节数组
     * @param dbPrintJob
     * @param ctx
     * @param version
     * @param foreseenId
     * @param dbContract
     * @param balanceDTO
     * @return
     */
    public byte[] buildForeseenResultBytes(PrintJob dbPrintJob,  ChannelHandlerContext ctx,  String foreseenId, Contract dbContract, PrintProgressInfo printProgressInfo)  {


        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char type;				 	 //0xB5
         unsigned char  operateID[2];
         unsigned char content;				     //加密内容: result(长度为2 1 成功) + version(6) + ForeseensResultDTO 的json
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))ForeseensResult, *ForeseensResult;
         */

        ForeseensFmRespDTO foreseensFmRespDTO = new ForeseensFmRespDTO();

        foreseensFmRespDTO.setContractCode(dbContract.getCode());
        foreseensFmRespDTO.setForeseenId(foreseenId);
        foreseensFmRespDTO.setConsolidate(String.valueOf(MoneyUtils.changeY2F(dbContract.getConsolidate())));
        foreseensFmRespDTO.setCurrent(String.valueOf(MoneyUtils.changeY2F(dbContract.getCurrent())));
        foreseensFmRespDTO.setServerDate(DateKit.formatDateYmdhms(new Date()));


        Integer printJobType = dbPrintJob.getType();
        foreseensFmRespDTO.setPrintJobType(printJobType);
        foreseensFmRespDTO.setPrintJobId(dbPrintJob.getId());
        foreseensFmRespDTO.setTotalAmount(String.valueOf(MoneyUtils.changeY2F(dbPrintJob.getTotalAmount())));
        foreseensFmRespDTO.setTotalCount(String.valueOf(dbPrintJob.getTotalCount()));

        //决定给机器发送什么内容
        if (printJobType == PrintJobTypeEnum.Machine.getCode()) {
            //机器的时候发特殊网页填写的地址列表
            foreseensFmRespDTO.setAddressList(contractAddressService.selectArrayByConractCode(dbContract.getCode()));
        }else {
            if (printProgressInfo == null){
                printProgressInfo = printJobService.getProductPrintProgress(dbPrintJob);
            }
            foreseensFmRespDTO.setPcUserId(String.valueOf(FebsUtil.getCurrentUser().getUserId()));
            //PC创建订单的时候，地址信息是保存在Products里面
            foreseensFmRespDTO.setProducts(printProgressInfo.getProductArr());
            foreseensFmRespDTO.setActualAmount(printProgressInfo.getActualAmount());
            foreseensFmRespDTO.setActualCount(printProgressInfo.getActualCount());
            foreseensFmRespDTO.setHasTranaction(StringUtils.isNotBlank(dbPrintJob.getTransactionId()) == true ? "1" : "0");
            //网络订单，告诉机器打印什么类型的戳
            foreseensFmRespDTO.setPrintObjectType(dbPrintJob.getPrintObjectType());
        }
//        log.info(JSON.toJSONString(printProgressInfo.getProductArr()));
        /*Arrays.stream(printProgressInfo.getProductArr()).forEach(item -> {
            log.info(item.toString());
        });*/
        String responseData = FMResultEnum.SUCCESS.getSuccessCode() + FebsConstant.FmVersion1 + JSON.toJSONString(foreseensFmRespDTO,  SerializerFeature.DisableCircularReferenceDetect);
        String tempKey = null;
        try {
            tempKey = tempKeyUtils.getTempKey(ctx);
        } catch (Exception e) {
            throw new FmException("tempkey获取失败");
        }
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("foreseens协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8);
    }

    /**
     * 保存批次信息
     * @param transactionMsgFmDto
     * @return
     */
    public String saveTransactionMsg(TransactionMsgFMDTO transactionMsgFmDto) {
        //测试代码
//        transactionMsgFmDto.setDmMsg("!45!01NE6431310001207210006000001010");

        log.info("解析得到的对象：TransactionFMDTO={}", transactionMsgFmDto.toString());

        //判断msg是否符合规范
        checkUtils.checkTransactionMsg(transactionMsgFmDto.getDmMsg().trim());

        //判断机器状态是否正常
//        checkUtils.checkFmEnable(transactionMsgFmDto.getFrankMachineId());

        return dmMsgService.saveMsg(transactionMsgFmDto);
    }

    /**
     * 【机器请求transactions协议】调用本方法
     * 交易
     */
    public Contract transactions(TransactionFMDTO transactionFmDto) {
        //测试修改数据
//        transactionFmDto.setPostOffice("131999");
//        transactionFmDto.setFrankMachineId("PM100501");
//        transactionFmDto.setContractCode("00001019");

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

        //检查transactionId是否存在
        Transaction dbTransaction = checkUtils.checkTransactionIdExist(transactionId);

        //获取需要发送给俄罗斯的数据
        TransactionDTO transactionDTO = getTransactionDTO(transactionFmDto, dbTransaction, dbContract);

        ApiRussiaResponse transactionsResponse = serviceInvokeRussia.transactions(transactionDTO);
        if (!transactionsResponse.isOK()) {
            if (transactionsResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                //todo 要考虑到这种情况，机器打印结束后，俄罗斯有问题，机器再次发送的情况
                printJobService.changeTransactionStatus(dbPrintJob, dbContract, transactionDTO, FlowDetailEnum.JobingErrorTransactionUnKnow,null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "transactionsResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                printJobService.changeTransactionStatus(dbPrintJob,  dbContract, transactionDTO, FlowDetailEnum.JobingErrorTransaction4xx, null);
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

    private TransactionDTO getTransactionDTO(TransactionFMDTO transactionFMDTO, Transaction dbTransaction, Contract dbContract) {
        //机器不让欠钱，暂时为0
        transactionFMDTO.setCreditVal("0");
        //数据库得到具体的dmMsg信息
        DmMsgDetail dmMsgDetail = dmMsgService.getDmMsgDetailAfterFinishJob(dbTransaction.getId(), false);
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
        transactionDTO.setStartDateTime(dbTransaction.getStartDateTime());
        transactionDTO.setStopDateTime(DateKit.createRussiatime(new Date()));

        //处理金额
        transactionDTO.setAmount(fmAmount);
        transactionDTO.setCreditVal(fmCreditVal);
        transactionDTO.setGraphId("");

        /*//处理UserId
        String userId = getUserIdByContractCode(transactionDTO.getContractCode());
        transactionDTO.setUserId(userId);*/
        return transactionDTO;
    }

    /**
     * 【机器请求cancelJob协议】调用本方法
     * 取消任务
     */
    public Contract cancelJob(CancelJobFMDTO cancelJobFmDto) {
        String operationName = "cancelJob";
        String frankMachineId = cancelJobFmDto.getFrankMachineId();
        String foreseenId = cancelJobFmDto.getForeseenId();
        String cancelMessage = CancelMsgEnum.getByCode(cancelJobFmDto.getCancelMsgCode()).getMsg();

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
                && curFlowDetail != FlowDetailEnum.JobingErrorForeseensCancelUnKnow
                && curFlowDetail != FlowDetailEnum.JobingErrorForeseensCancel4xx) {
            throw new FmException(FMResultEnum.OrderProcessIsNotRight.getCode(),"cancelJob 订单进度不符合条件，frankMachineId = " + frankMachineId + ", foreseenId = " + foreseenId + ", 当前进度为：" + curFlowDetail.getMsg());
        }

        //判断合同状态是否可用
        Contract dbContract = checkUtils.checkContractIsOk(dbPrintJob.getContractCode());


        //给俄罗斯发消息
        ApiRussiaResponse cancelResponse = serviceInvokeRussia.cancel(foreseenId,  cancelJobFmDto.getContractCode(), new ForeseenCancel(cancelMessage));
        if (!cancelResponse.isOK()) {
            if (cancelResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFmDto, FlowDetailEnum.JobingErrorForeseensCancelUnKnow, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId, operationName);
                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(),"cancelResponse.isOK() false ");
            } else {
                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFmDto, FlowDetailEnum.JobingErrorForeseensCancel4xx, null);
                log.error("服务器收到了设备{}发送的{}协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId, operationName);
                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(),"cancelResponse.isOK() false ");
            }
        }

        ManagerBalanceDTO balanceDTO = (ManagerBalanceDTO) cancelResponse.getObject();
        log.info("transactions 俄罗斯返回的ManagerBalanceDTO = {}", balanceDTO);

        printJobService.changeForeseensCancelStatus(dbPrintJob, cancelJobFmDto, FlowDetailEnum.JobEndFailForeseensCancelSuccess, balanceDTO);

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


    /**
     * 通知机器需要更新新的publickey
     * @param frankMachineId
     * @param dbPublicKey
     */
    public void noticeMachineUpdateKey(String frankMachineId, PublicKey dbPublicKey) {
        //异步：发送privateKey给机器
        log.info("得到俄罗斯的公钥请求/机器成功了auth请求，我们服务器创建了publickey对象，然后异步通知机器更新publickey然后发给服务器");
        serviceToMachineProtocol.noticeMachineUpdateKey(frankMachineId, dbPublicKey);
    }


}
