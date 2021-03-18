package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.entity.manager.ApiResponse;
import cc.mrbird.febs.asu.entity.service.*;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 被俄罗斯调用的接口
 */
@RestController
public class ServiceApi {
    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;


    /**
     * 公钥请求
     * @param frankMachineId
     * @param regenerate   要求重新创建私钥
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/publicKey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, boolean regenerate){

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
    public ApiResponse changeStatus(@PathVariable @NotBlank String frankMachineId, @RequestBody ChangeStatusRequest changeStatusRequest){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 将数据（更新）邮局到FM
     * @param postOffice
     * @return
     */
    @PutMapping("/postOffices")
    public ApiResponse postOffices(@RequestBody PostOffice postOffice){

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

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 发送合同数据
     * @param contract
     * @return
     */
    @PutMapping("/contracts")
    public ApiResponse contracts(@RequestBody Contract contract){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 合同余额的同步
     * @param contractId
     * @param balance
     * @return
     */
    @PutMapping("/contracts/{contractId}/balance")
    public ApiResponse contracts(@PathVariable @NotNull String contractId , @RequestBody Balance balance){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(400, new Error());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

}
