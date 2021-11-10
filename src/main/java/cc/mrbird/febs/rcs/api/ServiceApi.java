package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.annotation.CheckIpWhiteList;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.service.*;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 被俄罗斯调用的接口
 * TODO 需要添加安全验证
 * TODO 需要防止大量访问
 * TODO 过滤IP，只接受一个IP
 *
 */
@Slf4j
@RequestMapping("/rcs-service")
@RestController
@Validated
public class ServiceApi {
    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    ITaxService taxService;

    @Autowired
    IPostOfficeService postOfficeService;

    @Autowired
    IContractService contractService;

    @Autowired
    IBalanceService balanceService;

    @Autowired
    IPublicKeyService publicKeyService;

    @Autowired
    IPrintJobService printJobService;

    @Autowired
    ServiceManageCenter serviceManageCenter;

    @Autowired
    @Qualifier(value = FebsConstant.ASYNC_POOL)
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 公钥请求
     * @param frankMachineId
     * @param regenerate   要求重新创建私钥
     * @return
     */
    @CheckIpWhiteList
    @PostMapping("/frankMachines/{frankMachineId}/publicKey")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_publickey")
    public void publicKey(@PathVariable @NotBlank String frankMachineId, boolean regenerate, HttpServletResponse response){
        log.info("【俄罗斯调用服务器api 开始 publicKey】");
        regenerate = true;
        log.info("frankMachineId={},regenerate={}",frankMachineId,regenerate);

        if (!deviceService.checkExistByFmId(frankMachineId)){
            throw new RcsApiException(RcsApiErrorEnum.UnknownFMId);
        }

        if (regenerate) {
            //如果打印任务没有结束，拒绝
            if(!printJobService.checkPrintJobFinish(frankMachineId)){
                throw new RcsApiException(RcsApiErrorEnum.WaitPrintJobFinish);
            }


            PublicKey publicKey = publicKeyService.findByFrankMachineId(frankMachineId);
            if (publicKey !=null && publicKey.getFlow() == FlowEnum.FlowIng.getCode()){
                log.info("privateKey正在处理中，请等待");
                throw new RcsApiException(RcsApiErrorEnum.WaitPublicKeyUpdateFinish);
            }

            //生成publickey，更新数据库，机器开机后，检查是需要改变publickey
            PublicKey dbPublicKey = publicKeyService.saveOrUpdatePublicKey(frankMachineId);
            serviceManageCenter.noticeMachineUpdateKey(frankMachineId, dbPublicKey);
        }
        log.info("【俄罗斯调用服务器api 结束 publicKey】");
    }

    /**
     * 更改FM状态（锁定/解锁）
     * @param frankMachineId
     * @param changeStatusRequestDTO
     * @return
     */
    @CheckIpWhiteList
    @PostMapping("/frankMachines/{frankMachineId}/changeStatus")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_changeStatus")
    public void changeStatus(@PathVariable @NotBlank String frankMachineId,
                                          @Validated @RequestBody ChangeStatusRequestDTO changeStatusRequestDTO, HttpServletResponse response) throws RuntimeException {
        log.info("【俄罗斯调用服务器api 开始 changeStatus】");
        log.info("俄罗斯 更改FM状态 frankMachineId = {} changeStatusRequestDTO={}",frankMachineId,changeStatusRequestDTO.toString());

        //如果打印任务没有结束，拒绝
        if(!printJobService.checkPrintJobFinish(frankMachineId)){
            throw new RcsApiException(RcsApiErrorEnum.WaitPrintJobFinish);
        }

        //保存要更改的状态
        deviceService.changeStatusBegin(frankMachineId, changeStatusRequestDTO);

        //在一个线程中执行：发送指令给FM
        serviceToMachineProtocol.changeStatus(frankMachineId, changeStatusRequestDTO);

        //执行到这就返回给俄罗斯
        log.info("【俄罗斯调用服务器api 结束 changeStatus】");
    }

    /**
     * 将数据（更新）邮局到FM
     * @param taxVersionDTO
     * @return
     */
    @CheckIpWhiteList
    @PutMapping("/taxes")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_taxes")
    public void taxes(@RequestBody @Validated TaxVersionDTO taxVersionDTO, HttpServletResponse response){
        log.info("【俄罗斯调用服务器api 开始 taxes】");
//        log.info("taxVersionDTO={}", JSON.toJSONString(taxVersionDTO));
        if (taxService.checkIsExist(taxVersionDTO.getVersion())){
            throw new RcsApiException(RcsApiErrorEnum.TaxVersionExist);
        }
        String jsonFileName = DateKit.getNowDateToFileName();
        //数据库保存信息
        if(taxService.saveTaxVersion(taxVersionDTO, jsonFileName)){
            //告知俄罗斯已经收到 异步
            threadPoolTaskExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    serviceManageCenter.rateTables(taxVersionDTO.getVersion());
                    serviceToMachineProtocol.sendTaxToAllMachine(taxVersionDTO, jsonFileName);
                }
            });
        }else{
            throw new RcsApiException(RcsApiErrorEnum.SaveTaxVersionError);
        }

        log.info("【俄罗斯调用服务器api 结束 taxes】");
    }

    /**
     * 接收邮局信息
     * @param postOfficeDTO
     * @return
     */
    @CheckIpWhiteList
    @PutMapping("/postOffices")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_postOffices")
    public void postOffices(@RequestBody @Validated PostOfficeDTO postOfficeDTO, HttpServletResponse response){
        log.info("【俄罗斯调用服务器api 开始 postOffices】");
        log.info("postOfficeDTO={}",postOfficeDTO.toString());
        postOfficeService.savePostOfficeDTO(postOfficeDTO);
        log.info("【俄罗斯调用服务器api 结束 postOffices】");
    }

    /**
     * 接收服务器传递过来的合同数据
     * @param contractDTO
     * @return
     */
    @CheckIpWhiteList
    @PutMapping("/contracts")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_contracts")
    public void contracts(@RequestBody @Validated ContractDTO contractDTO, HttpServletResponse response){
        log.info("【俄罗斯调用服务器api 开始 contracts】");
        log.info("contractDTO={}",contractDTO.toString());
        contractService.saveContractDto(contractDTO);
        log.info("【俄罗斯调用服务器api 结束 contracts】");
    }

    /**
     * 合同余额的同步
     * @param code
     * @param serviceBalanceDTO
     * @return
     */
    @CheckIpWhiteList
    @PutMapping("/contracts/{code}/balance")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_balance")
    public void balance(@PathVariable @NotNull String code , @RequestBody @Validated ServiceBalanceDTO serviceBalanceDTO, HttpServletResponse response){
        log.info("【俄罗斯调用服务器api 开始 balance】");
        log.info("code={}, serviceBalanceDTO={}",code, serviceBalanceDTO.toString());
        balanceService.saveBalance(code, serviceBalanceDTO);
        log.info("【俄罗斯调用服务器api 结束 balance】");
    }

}
