package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.entity.manager.*;
import cc.mrbird.febs.asu.entity.manager.Error;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ManagerApi {
    /**
     * 机器状况
     */
    @PutMapping("frankMachines")
    public ApiResponse frankMachines(FrankMachine frankMachine){
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * FM授权请求
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/auth")
    public ApiResponse auth(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachine frankMachine){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 取消授权
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/unauth")
    public ApiResponse unauth(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachine frankMachine){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 取消授权
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/lost")
    public ApiResponse lost(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachine frankMachine){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 提交新的加密密钥
     * @param frankMachineId
     * @param PublicKey
     * @return
     */
    @PutMapping("/frankMachines/{frankMachineId}/publicKey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, @RequestBody PublicKey PublicKey){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 资费表加载结果
     * @param rateTableFeedback
     * @return
     */
    @PutMapping("/rateTables")
    public ApiResponse rateTables(@RequestBody RateTableFeedback rateTableFeedback){
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 要求根据预报生产法郎
     * @param foreseen
     * @return
     */
    @PostMapping("/foreseens")
    public ApiResponse foreseens(@RequestBody Foreseen foreseen){
        ApiResponse apiResponse =  new ApiResponse(200, new Balance());
        apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    /**
     * 取消工作任务
     * @param foreseenId
     * @param foreseenCancel
     * @return
     */
    @PostMapping("/foreseens/{foreseenId}/cancel")
    public ApiResponse cancel(@PathVariable String foreseenId, @RequestBody ForeseenCancel foreseenCancel){
        ApiResponse apiResponse =  new ApiResponse(200, new Balance());
        apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    @PostMapping("/transactions")
    public ApiResponse transactions(@NotNull @RequestBody Transaction transaction){
        ApiResponse apiResponse =  new ApiResponse(200, new Balance());
        apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    @PostMapping("/refills")
    public ApiResponse refills(@RequestBody Registers registers){
        ApiResponse apiResponse =  new ApiResponse(200, new Balance());
        apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

    @PostMapping("/franking/stats")
    public ApiResponse stats(@RequestBody Statistics statistics){
        ApiResponse apiResponse =  new ApiResponse(200, new Balance());
        apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new Error());
        return apiResponse;
    }

}
