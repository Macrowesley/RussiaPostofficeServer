package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.dto.manager.ApiResponse;
import cc.mrbird.febs.rcs.dto.service.*;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    ServiceInvokeRussia serviceInvokeRussia;

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
    RedisService redisService;

    @Autowired
    IPrintJobService printJobService;


    /**
     * 公钥请求
     * @param frankMachineId
     * @param regenerate   要求重新创建私钥
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/publicKey")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_publickey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, boolean regenerate){
        log.info("【俄罗斯调用服务器api 开始 publicKey】");
        log.info("frankMachineId={},regenerate={}",frankMachineId,regenerate);

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

            //生成publickey，更新数据库
            PublicKey dbPublicKey = publicKeyService.saveOrUpdatePublicKey(frankMachineId);
            //异步：发送privateKey给机器
            log.info("得到俄罗斯的公钥请求，我们服务器更新了publickey，然后异步把最新的privateKey给机器");
            serviceToMachineProtocol.sentPrivateKeyInfo(frankMachineId, dbPublicKey);
        }
        log.info("【俄罗斯调用服务器api 结束 publicKey】");
        return new ApiResponse(200, "ok");
    }

    /**
     * 更改FM状态（锁定/解锁）
     * @param frankMachineId
     * @param changeStatusRequestDTO
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/changeStatus")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_changeStatus")
    public ApiResponse changeStatus(@PathVariable @NotBlank String frankMachineId,
                                    @Validated @RequestBody ChangeStatusRequestDTO changeStatusRequestDTO) throws RuntimeException {
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
        return new ApiResponse(200, "ok");
    }

    /**
     * 将数据（更新）邮局到FM
     * @param taxVersionDTO
     * @return
     */
    @PutMapping("/taxes")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_taxes")
    public ApiResponse taxes(@RequestBody @Validated TaxVersionDTO taxVersionDTO){
        log.info("【俄罗斯调用服务器api 开始 taxes】");
        log.info("taxVersionDTO={}",taxVersionDTO.toString());
        //数据库保存信息
        taxService.saveTaxVersion(taxVersionDTO);

        //todo 目前只保存，接下来如何处理得看安排，不能直接通知机器更新版本信息
//        serviceToMachineProtocol.updateTaxes(taxVersionDTO);
        log.info("【俄罗斯调用服务器api 结束 taxes】");
        return new ApiResponse(200, "ok");
    }

    /**
     * 接收邮局信息
     * @param postOfficeDTO
     * @return
     */
    @PutMapping("/postOffices")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_postOffices")
    public ApiResponse postOffices(@RequestBody @Validated PostOfficeDTO postOfficeDTO){
        log.info("【俄罗斯调用服务器api 开始 postOffices】");
        log.info("postOfficeDTO={}",postOfficeDTO.toString());
        postOfficeService.savePostOfficeDTO(postOfficeDTO);
        log.info("【俄罗斯调用服务器api 结束 postOffices】");
        return new ApiResponse(200, "ok");
    }

    /**
     * 接收服务器传递过来的合同数据
     * @param contractDTO
     * @return
     */
    @PutMapping("/contracts")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_contracts")
    public ApiResponse contracts(@RequestBody @Validated ContractDTO contractDTO){
        log.info("【俄罗斯调用服务器api 开始 contracts】");
        log.info("contractDTO={}",contractDTO.toString());
        contractService.saveContractDto(contractDTO);
        log.info("【俄罗斯调用服务器api 结束 contracts】");
        return new ApiResponse(200, "ok");
    }

    /**
     * 合同余额的同步
     * @param contractId
     * @param serviceBalanceDTO
     * @return
     */
    @PutMapping("/contracts/{contractId}/balance")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_service_api_balance")
    public ApiResponse balance(@PathVariable @NotNull String contractId , @RequestBody @Validated ServiceBalanceDTO serviceBalanceDTO){
        log.info("【俄罗斯调用服务器api 开始 balance】");
        log.info("contractId={}, serviceBalanceDTO={}",contractId, serviceBalanceDTO.toString());
        balanceService.saveBalance(contractId, serviceBalanceDTO);
        log.info("【俄罗斯调用服务器api 结束 balance】");
        return new ApiResponse(200, "ok");
    }

}
