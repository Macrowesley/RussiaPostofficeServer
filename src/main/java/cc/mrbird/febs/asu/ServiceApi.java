package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.entity.manager.ApiResponse;
import cc.mrbird.febs.asu.entity.manager.PublicKey;
import cc.mrbird.febs.asu.entity.service.*;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
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
        PublicKey publicKey = new PublicKey();
        ApiResponse response = serviceInvokeManager.publicKey(frankMachineId, publicKey);
        if (response.isOK()){
            //todo 发送了public，更新步骤

        }
        //线程中


        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 更改FM状态（锁定/解锁）
     * @param frankMachineId
     * @param changeStatusRequest
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/changeStatus")
    public ApiResponse changeStatus(@PathVariable @NotBlank String frankMachineId, @RequestBody ChangeStatusRequest changeStatusRequest) throws AsuApiException {
        //TODO 检查请求的有效性
        if (StringUtils.isEmpty(frankMachineId)){
            //TODO 拦截这种错误，发送错误请求给服务器
            throw new AsuApiException("error");
        }

        //TODO 想想有没有其他需要验证的

        //todo 【收到了服务器消息】

        //todo 在一个线程中执行：发送指令给FM
        //线程中
        serviceToMachineProtocol.changeStatus(frankMachineId,changeStatusRequest);
        //线程中

        //TODO 如果都没问题，最后返回200
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 接收邮局信息
     * @param postOffice
     * @return
     */
    @PutMapping("/postOffices")
    public ApiResponse postOffices(@RequestBody PostOffice postOffice){
        /**
         * todo 接收邮局信息
         * 1. 更新邮局信息
         * 2. 更新邮局和合同的关系表
         */
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 将数据（更新）邮局到FM
     * @param taxVersion
     * @return
     */
    @PutMapping("/taxes")
    public ApiResponse taxes(@RequestBody TaxVersion taxVersion){
        //todo 【收到了服务器消息】

        //todo 保存信息到数据库


        //todo 在一个线程中执行：发送指令给所有的FM，FM不在线怎么办？
        //todo
        //线程中
        serviceToMachineProtocol.updateTaxes(taxVersion);
        //线程中


        //todo 返回结果

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 接收服务器传递过来的合同数据
     * @param contract
     * @return
     */
    @PutMapping("/contracts")
    public ApiResponse contracts(@RequestBody Contract contract){
        /**
         * TODO 接收服务器传递过来的合同数据
         * 1. 合同插入数据库
         *
         */
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 合同余额的同步
     * @param contractId
     * @param serviceBalance
     * @return
     */
    @PutMapping("/contracts/{contractId}/balance")
    public ApiResponse contracts(@PathVariable @NotNull String contractId , @RequestBody ServiceBalance serviceBalance){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

}
