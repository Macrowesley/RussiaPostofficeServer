package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.dto.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/test/manager")
public class ManagerApi {
    /**
     * 机器状况
     */
    @PutMapping("frankMachines")
    public ApiResponse frankMachines(@Validated @RequestBody FrankMachine frankMachine, HttpServletRequest request){
        log.info("收到消息：{}", frankMachine.toString());
        log.info("收到的header X-API-KEY={}", request.getHeader("X-API-KEY"));
        frankMachine.setId("666 manager收到了");
        int code = 400;
        ApiResponse apiResponse;
        switch (code){
            case 200:
                apiResponse =  new ApiResponse(200, frankMachine);
                break;
            case 400:
                ManagerBalance managerBalance = new ManagerBalance();
                managerBalance.setBalanceId("11");
                managerBalance.setContractId("222");
                managerBalance.setContractNum(333);
                managerBalance.setCurrent(444.0D);
                managerBalance.setConsolidate(555.0D);
                apiResponse =  new ApiResponse(400, new OperationError(400,"操作失误111", managerBalance));

                break;
            case 500:
                apiResponse =  new ApiResponse(500, new ApiError(500,"网络问题111"));

                break;
            default:
                apiResponse =  new ApiResponse(500, new ApiError(500,"网络问题111"));
                break;
        }

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
        log.info("manager auth frankMachineId={}",frankMachineId);
        log.info("manager auth frankMachine={}",frankMachine.toString());

        frankMachine.setId("manager auth 新的id");
        ApiResponse apiResponse =  new ApiResponse(200, "ok");

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
        apiResponse =  new ApiResponse(500, new ApiError());
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
        log.info("manager lost frankMachineId={}", frankMachineId);
        log.info("manager lost frankMachine={}", frankMachine);
        ApiResponse apiResponse =  new ApiResponse(200, "ok 666");
        apiResponse =  new ApiResponse(500, new ApiError(500, "错误"));
        return apiResponse;
    }

    /**
     * 提交新的加密密钥
     * @param frankMachineId
     * @param PublicKey
     * @return
     */
    @PutMapping("/frankMachines/{frankMachineId}/publicKey")
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, @RequestBody PublicKey publicKey){
        log.info("manager lost frankMachineId={}", frankMachineId);
        log.info("manager lost publicKey={}", publicKey.toString());
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
//        apiResponse =  new ApiResponse(500, new ApiError(200,"error hahaha"));
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
        log.info("manager rateTables rateTableFeedback={}", rateTableFeedback.toString());
//        apiResponse =  new ApiResponse(500, new ApiError(200,"error hahaha"));
        return apiResponse;
    }

    /**
     * 要求根据预报生产法郎
     * @param foreseen
     * @return
     */
    @PostMapping("/foreseens")
    public ApiResponse foreseens(@RequestBody Foreseen foreseen){
        log.info("manager foreseen foreseen={}", foreseen.toString());
        ManagerBalance managerBalance = new ManagerBalance();
        managerBalance.setBalanceId("11");
        managerBalance.setContractId("222");
        managerBalance.setContractNum(333);
        managerBalance.setCurrent(444.0D);
        managerBalance.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalance);
        /*apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    /**
     * 取消工作任务
     * @param foreseenId
     * @param foreseenCancel
     * @return
     */
    @PostMapping("/foreseens/{foreseenId}/cancel")
    public ApiResponse cancel(@PathVariable @NotBlank String foreseenId, @RequestBody ForeseenCancel foreseenCancel){

        log.info("manager cancel foreseenCancel={}", foreseenCancel.toString());
        log.info("manager cancel foreseenId={}", foreseenId);

        ManagerBalance managerBalance = new ManagerBalance();
        managerBalance.setBalanceId("11");
        managerBalance.setContractId("222");
        managerBalance.setContractNum(333);
        managerBalance.setCurrent(444.0D);
        managerBalance.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalance);

 /*       apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/transactions")
    public ApiResponse transactions(@NotNull @RequestBody Transaction transaction){
        log.info("manager transaction transaction={}", transaction.toString());

        ManagerBalance managerBalance = new ManagerBalance();
        managerBalance.setBalanceId("11");
        managerBalance.setContractId("222");
        managerBalance.setContractNum(333);
        managerBalance.setCurrent(444.0D);
        managerBalance.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalance);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/refills")
    public ApiResponse refills(@RequestBody Registers registers){
        log.info("manager registers registers={}", registers.toString());
        ManagerBalance managerBalance = new ManagerBalance();
        managerBalance.setBalanceId("11");
        managerBalance.setContractId("222");
        managerBalance.setContractNum(333);
        managerBalance.setCurrent(444.0D);
        managerBalance.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalance);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/franking/stats")
    public ApiResponse stats(@RequestBody Statistics statistics){
        log.info("manager statistics statistics={}", statistics.toString());

        ManagerBalance managerBalance = new ManagerBalance();
        managerBalance.setBalanceId("11");
        managerBalance.setContractId("222");
        managerBalance.setContractNum(333);
        managerBalance.setCurrent(444.0D);
        managerBalance.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalance);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

}
