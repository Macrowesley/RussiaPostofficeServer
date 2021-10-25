package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.FileUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.service.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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

    @Autowired
    RedisService redisService;

    /**
     * 校验机器是否可用
     * @param frankMachineId
     * @param serviceManageCenter
     */
    public void checkFmEnable(String frankMachineId) {
        if (StringUtils.isEmpty(frankMachineId)) {
            throw new FmException(FMResultEnum.SomeInfoIsEmpty.getCode(), "foreseens 信息缺失");
        }

        Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(frankMachineId);

        Integer dbCurFmStatus = dbDevice.getCurFmStatus();
        FMStatusEnum dbFmStatus = FMStatusEnum.getByCode(dbCurFmStatus);
        if (dbCurFmStatus == FMStatusEnum.UNAUTHORIZED.getCode() || dbCurFmStatus == FMStatusEnum.LOST.getCode() || dbCurFmStatus == FMStatusEnum.BLOCKED.getCode()) {
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

        //验证打印数量累计数据类型是否正确
        try {
            int firstPos = 29;
            int endPos = 37;
            int pieceCount = Integer.valueOf(dmMsg.substring(firstPos, endPos));
        }catch (Exception e){
            throw new FmException(FMResultEnum.DmmsgTotalPieceError);
        }

    }

    public Transaction checkTransactionIdExist(String transactionId) {
        Transaction transaction = printJobService.getTransactionById(transactionId);
        if (transaction == null){
            throw new FmException(FMResultEnum.TransactionIdNoExist);
        }
        return transaction;
    }

    /**
     * 判断机器发来的tax版本是否可行
     * 有必要的时候，会主动发送机器新的税率版本
     * @param fmTaxVersion
     */
    public void checkTaxIsOk(String frankMachineId, ChannelHandlerContext ctx, String fmTaxVersion, String machineDate) {
        /**
         机器发送来一个税率版本，和一个时间，根据这个税率版本，从数据库找到对应的版本，找到时间，判断
             当前系统时间 < applyDate
                未到生效时间，不能通过
             当前系统时间 = applyDate
                到了生效时间，可以通过
             当前系统时间 > applyDate_A
                 找到数据库中，比applyDate_A大的applyDate
                     没有比applyDate_A大的applyDate——现在的版本就是最新的
                        没问题
                     有比applyDate_A大的applydate
                        当前时间比这些时间要小
                            没问题
                        当前时间比这些时间中的某个大
                            有问题，机器发送的是旧版本的税率，需要更新
         */

        Tax dbTax = taxService.findTaxByVersion(fmTaxVersion);
        if (dbTax == null) {
//            throw new FmException(FMResultEnum.TaxVersionNotExist.getCode(),"taxversion " + fmTaxVersion + "数据库中不存在");
            sendUnreceivedTaxsToMacine(frankMachineId, ctx);
            return;
        }

        Date curDate = new Date();
        Date applyDate = dbTax.getApplyDate();

        if (!DateKit.checkApplyDateIsEnable(curDate,applyDate)){
            throw new FmException(FMResultEnum.TaxApplyDateNotEnable.getCode(), "未到生效时间，不能通过，fmTaxVersion="+fmTaxVersion+", curDate="+ DateKit.formatDateyyyy_MM_dd_HH_mm_ss(curDate) + ", applyDate = " + DateKit.formatDateyyyy_MM_dd_HH_mm_ss(applyDate));
        }

        if (curDate.compareTo(applyDate) == 0) {
            log.info("到了生效时间，可以通过");
            return;
        }

        //找到数据库中，有没有发送给机器的tax
        sendUnreceivedTaxsToMacine(frankMachineId, ctx);
    }

    private void sendUnreceivedTaxsToMacine(String frankMachineId, ChannelHandlerContext ctx) {
        List<Tax> unreceivedTaxList = taxService.selectUnreceivedTaxListByFmId(frankMachineId);
        log.info("需要发给机器{}的税率版本有{}个", frankMachineId, unreceivedTaxList.size());
        if (unreceivedTaxList.size() > 0) {
            unreceivedTaxList.stream().forEach(tax -> {
                String version = tax.getVersion();
                log.info("发送{}版本给机器{}", version,frankMachineId);
                String savePath = tax.getSavePath();
                String jsonFileName = savePath.substring(savePath.lastIndexOf("\\") + 1, savePath.length() - 5);
                String jsonContent = (String) redisService.get(version);
                if (jsonContent == null){
                    log.info("redis没有{}这个缓存，从硬盘中找文件，提取内容",version);
                    jsonContent = FileUtil.readContent(savePath);
                }
                serviceToMachineProtocol.sendTaxesToMachine(frankMachineId, ctx, version, tax.getApplyDate(), jsonContent, jsonFileName, true);
            });
        }
    }

    public static void main(String[] args) {
        String savePath = "D:\\workspace\\\\PostmartOfficeServiceFile\\tax\\2021_10_27_15_25_37.json";
        String jsonFileName = savePath.substring(savePath.lastIndexOf("\\") + 1, savePath.length() - 5);
        log.info(jsonFileName);
    }
}
