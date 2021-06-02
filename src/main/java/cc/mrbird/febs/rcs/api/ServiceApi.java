package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.dto.manager.ApiResponse;
import cc.mrbird.febs.rcs.dto.manager.PublicKeyDTO;
import cc.mrbird.febs.rcs.dto.manager.RateTableFeedbackDTO;
import cc.mrbird.febs.rcs.dto.service.*;
import cc.mrbird.febs.rcs.service.*;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

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
    ServiceInvokeManager serviceInvokeManager;

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
    @Qualifier(FebsConstant.ASYNC_POOL)
    ThreadPoolTaskExecutor poolTaskExecutor;

    /**
     * 公钥请求
     * @param frankMachineId
     * @param regenerate   要求重新创建私钥
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/publicKey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, boolean regenerate){

        if (regenerate) {
            //生成publickey，更新数据库
            PublicKeyDTO publicKeyDTO = publicKeyService.saveOrUpdatePublicKey(frankMachineId);

            //异步：线程中调用俄罗斯的publickey接口，把public传递过去
            log.info("得到俄罗斯的公钥请求，我们服务器更新了publickey，然后异步把最新的publickey发送给了俄罗斯");
            poolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse response = serviceInvokeManager.publicKey(frankMachineId, publicKeyDTO);
                    log.info("得到俄罗斯的公钥请求，我们服务器更新了publickey，然后异步把最新的publickey发送给了俄罗斯，得到俄罗斯返回的结果：{}", response.toString());
                }
            });
        }

        return new ApiResponse(200, "ok");
    }

    /**
     * 更改FM状态（锁定/解锁）
     * @param frankMachineId
     * @param changeStatusRequestDTO
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/changeStatus")
    public ApiResponse changeStatus(@PathVariable @NotBlank String frankMachineId,
                                    @Validated @RequestBody ChangeStatusRequestDTO changeStatusRequestDTO) throws RuntimeException {
        log.info("更改FM状态 frankMachineId = {} changeStatusRequestDTO={}",frankMachineId,changeStatusRequestDTO.toString());

        //保存要更改的状态
        deviceService.changeStatusBegin(frankMachineId, changeStatusRequestDTO);

        //在一个线程中执行：发送指令给FM
        serviceToMachineProtocol.changeStatus(frankMachineId, changeStatusRequestDTO);

        //执行到这就返回给俄罗斯
        return new ApiResponse(200, "ok");
    }

    /**
     * 将数据（更新）邮局到FM
     * @param taxVersionDTO
     * @return
     */
    @PutMapping("/taxes")
    public ApiResponse taxes(@RequestBody @Validated TaxVersionDTO taxVersionDTO){
        //数据库保存信息
        taxService.saveTaxVersion(taxVersionDTO);

        //todo 目前只保存，接下来如何处理得看安排，不能直接通知机器更新版本信息
//        serviceToMachineProtocol.updateTaxes(taxVersionDTO);
        return new ApiResponse(200, "ok");
    }

    /**
     * 接收邮局信息
     * @param postOfficeDTO
     * @return
     */
    @PutMapping("/postOffices")
    public ApiResponse postOffices(@RequestBody @Validated PostOfficeDTO postOfficeDTO){
        postOfficeService.savePostOfficeDTO(postOfficeDTO);
        return new ApiResponse(200, "ok");
    }

    /**
     * 接收服务器传递过来的合同数据
     * @param contractDTO
     * @return
     */
    @PutMapping("/contracts")
    public ApiResponse contracts(@RequestBody @Validated ContractDTO contractDTO){
        contractService.saveContractDto(contractDTO);
        return new ApiResponse(200, "ok");
    }

    /**
     * 合同余额的同步
     * @param contractId
     * @param serviceBalanceDTO
     * @return
     */
    @PutMapping("/contracts/{contractId}/balance")
    public ApiResponse balance(@PathVariable @NotNull String contractId , @RequestBody @Validated ServiceBalanceDTO serviceBalanceDTO){
        balanceService.saveBalance(contractId, serviceBalanceDTO);
        return new ApiResponse(200, "ok");
    }

}
