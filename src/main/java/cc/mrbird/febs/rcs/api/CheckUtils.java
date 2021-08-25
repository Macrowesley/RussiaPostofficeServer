package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class CheckUtils {
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

    /**
     * 校验机器是否可用
     * @param frankMachineId
     * @param serviceManageCenter
     */
    public void checkFmEnable(String frankMachineId) {
        if (StringUtils.isEmpty(frankMachineId)) {
            throw new FmException(FMResultEnum.SomeInfoIsEmpty.getCode(), "foreseens 信息缺失");
        }

        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        Integer dbCurFmStatus = dbDevice.getCurFmStatus();
        FMStatusEnum dbFmStatus = FMStatusEnum.getByCode(dbCurFmStatus);
        if (dbCurFmStatus == FMStatusEnum.UNAUTHORIZED.getCode() || dbCurFmStatus == FMStatusEnum.LOST.getCode()) {
            throw new FmException(FMResultEnum.StatusNotValid.getCode(), "foreseens 机器状态不正常，当前状态为：" + dbFmStatus.getStatus());
        }
    }

    /**
     * 校验postoffice是否存在
     * @param postOffice
     */
    public void checkPostOfficeExist(String postOffice) {
        log.info("postOffice:"+postOffice);
        if(!postOfficeService.checkPostOfficeExist(postOffice)){
            throw new FmException(FMResultEnum.PostOfficeNoExist.getCode(),"PostOffice is not exist");
        }
    }

    /**
     * 判断合同状态是否可用
     * @param contractCode
     */
    public Contract checkContractIsOk(String contractCode) {
        Contract dbContract = contractService.getByConractCode(contractCode);
        if (dbContract == null) {
            log.error("Unknown contractCode:" + contractCode);
            throw new FmException(FMResultEnum.ContractNotExist);
        }

        Integer dbEnable = dbContract.getEnable();
        if (dbEnable == ContractEnableEnum.UNENABLE.getCode()) {
            throw new FmException("transactions 订单状态不可用，当前订单的状态为：" + dbEnable);
        }

        return dbContract;
    }

    /**
     * 判断订单状态是否符合条件：
     * 只有以下2种情况才能执行transaction： JobingForeseensSuccess 或者 JobErrorTransactionUnKnow
     * @param foreseenId
     * @return
     */
    public PrintJob checkTransactionFlowDetailIsOk(String foreseenId, String frankMachineId) {
        PrintJob dbPrintJob = printJobService.getByForeseenId(foreseenId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());

        if (curFlowDetail != FlowDetailEnum.JobingForeseensSuccess &&
                curFlowDetail != FlowDetailEnum.JobErrorTransactionUnKnow
                && curFlowDetail != FlowDetailEnum.JobErrorTransaction4xx) {
            throw new FmException(FMResultEnum.OrderProcessIsNotRight.getCode(), "transactions 订单进度不符合条件，frankMachineId = " + frankMachineId + ", foreseenId = " + foreseenId + ", 当前进度为：" + curFlowDetail.getMsg());
        }
        return dbPrintJob;
    }

    /**
     * 校验FM id是否存在
     * @param frankMachineId
     */
    public void checkFmIdExist(String frankMachineId) {
        if (!deviceService.checkExistByFmId(frankMachineId)) {
            throw new FmException(FMResultEnum.DeviceNotFind);
        }
    }

    /**
     * 判断dmMsg是否规范
     * @param dmMsg
     */
    public void checkTransactionMsg(String dmMsg) {
        if (StringUtils.isEmpty(dmMsg)){
            throw new FmException(FMResultEnum.DmmsgIsEmpty);
        }

        if (dmMsg.length() != 60){
            throw new FmException(FMResultEnum.DmmsgLengthError);
        }
    }
}
