package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.rcs.common.exception.RcsManagerBalanceException;
import cc.mrbird.febs.rcs.common.exception.RcsServiceApiException;
import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.rcs.dto.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ServiceInvokeManager serviceInvokeManager;


    /**
     * 公钥请求
     * @param frankMachineId
     * @param regenerate   要求重新创建私钥
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/publicKey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, boolean regenerate){
        //todo 【收到了服务器消息】

        //todo 生成publickey，更新数据库

        //todo 线程中调用俄罗斯的publickey接口，把public传递过去
        //线程中
        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
        ApiResponse response = serviceInvokeManager.publicKey(frankMachineId, publicKeyDTO);
        if (response.isOK()){
            //todo 发送了public，更新步骤

        }
        //线程中


        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
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
        if (true){
            /*return new ApiResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    new OperationError(
                            HttpStatus.BAD_REQUEST.value(),
                            "金额返回咯",
                            new ManagerBalanceDTO("111", "222", 100, 1000D, 2000D)
                    )
            );*/
            throw new RcsManagerBalanceException("金额有问题",new ManagerBalanceDTO("111", "222", 100, 1000D, 2000D));
        }
        //TODO 想想有没有其他需要验证的

        //todo 【收到了服务器消息】

        //todo 在一个线程中执行：发送指令给FM
        //线程中
        serviceToMachineProtocol.changeStatus(frankMachineId, changeStatusRequestDTO);
        //线程中

        //TODO 如果都没问题，最后返回200
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
      /*  apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    /**
     * 接收邮局信息
     * @param postOfficeDTO
     * @return
     */
    @PutMapping("/postOffices")
    public ApiResponse postOffices(@RequestBody @Validated PostOfficeDTO postOfficeDTO){
        /**
         * todo 接收邮局信息
         * 1. 更新邮局信息
         * 2. 更新邮局和合同的关系表
         */
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
    }

    /**
     * 将数据（更新）邮局到FM
     * @param taxVersionDTO
     * @return
     */
    @PutMapping("/taxes")
    public ApiResponse taxes(@RequestBody @Validated TaxVersionDTO taxVersionDTO){
        //todo 【收到了服务器消息】

        //todo 保存信息到数据库


        //todo 在一个线程中执行：发送指令给所有的FM，FM不在线怎么办？
        //todo
        //线程中
        serviceToMachineProtocol.updateTaxes(taxVersionDTO);
        //线程中


        //todo 返回结果

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
    }

    /**
     * 接收服务器传递过来的合同数据
     * @param contractDTO
     * @return
     */
    @PutMapping("/contracts")
    public ApiResponse contracts(@RequestBody @Validated ContractDTO contractDTO){
        /**
         * TODO 接收服务器传递过来的合同数据
         * 1. 合同插入数据库
         *
         */
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
    }

    /**
     * 合同余额的同步
     * @param contractId
     * @param serviceBalanceDTO
     * @return
     */
    @PutMapping("/contracts/{contractId}/balance")
    public ApiResponse contracts(@PathVariable @NotNull String contractId , @RequestBody @Validated ServiceBalanceDTO serviceBalanceDTO){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new ApiError());
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
    }

}
