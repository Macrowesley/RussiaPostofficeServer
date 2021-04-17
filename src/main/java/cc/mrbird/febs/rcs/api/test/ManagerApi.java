package cc.mrbird.febs.rcs.api.test;

import cc.mrbird.febs.rcs.dto.manager.*;
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
    public ApiResponse frankMachines(@Validated @RequestBody FrankMachineDTO frankMachineDTO, HttpServletRequest request){
        log.info("收到消息：{}", frankMachineDTO.toString());
        log.info("收到的header X-API-KEY={}", request.getHeader("X-API-KEY"));
        frankMachineDTO.setId("666 manager收到了");
        int code = 400;
        ApiResponse apiResponse;
        switch (code){
            case 200:
                apiResponse =  new ApiResponse(200, frankMachineDTO);
                break;
            case 400:
                ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
                managerBalanceDTO.setBalanceId("11");
                managerBalanceDTO.setContractId("222");
                managerBalanceDTO.setContractNum(333);
                managerBalanceDTO.setCurrent(444.0D);
                managerBalanceDTO.setConsolidate(555.0D);
                apiResponse =  new ApiResponse(400, new OperationError(400,"操作失误111", managerBalanceDTO));

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
     * @param frankMachineDTO
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/auth")
    public ApiResponse auth(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachineDTO frankMachineDTO){
        log.info("manager auth frankMachineId={}",frankMachineId);
        log.info("manager auth frankMachine={}", frankMachineDTO.toString());

        frankMachineDTO.setId("manager auth 新的id");
        ApiResponse apiResponse =  new ApiResponse(200, "ok");

        return apiResponse;
    }

    /**
     * 取消授权
     * @param frankMachineId
     * @param frankMachineDTO
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/unauth")
    public ApiResponse unauth(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachineDTO frankMachineDTO){

        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        apiResponse =  new ApiResponse(500, new ApiError());
        return apiResponse;
    }

    /**
     * 取消授权
     * @param frankMachineId
     * @param frankMachineDTO
     * @return
     */
    @PostMapping("/frankMachines/{frankMachineId}/lost")
    public ApiResponse lost(@PathVariable @NotBlank String frankMachineId, @RequestBody FrankMachineDTO frankMachineDTO){
        log.info("manager lost frankMachineId={}", frankMachineId);
        log.info("manager lost frankMachine={}", frankMachineDTO);
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
    public ApiResponse publicKey(@PathVariable @NotBlank String frankMachineId, @RequestBody PublicKeyDTO publicKeyDTO){
        log.info("manager lost frankMachineId={}", frankMachineId);
        log.info("manager lost publicKey={}", publicKeyDTO.toString());
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
//        apiResponse =  new ApiResponse(500, new ApiError(200,"error hahaha"));
        return apiResponse;
    }

    /**
     * 资费表加载结果
     * @param rateTableFeedbackDTO
     * @return
     */
    @PutMapping("/rateTables")
    public ApiResponse rateTables(@RequestBody RateTableFeedbackDTO rateTableFeedbackDTO){
        ApiResponse apiResponse =  new ApiResponse(200, "ok");
        log.info("manager rateTables rateTableFeedback={}", rateTableFeedbackDTO.toString());
//        apiResponse =  new ApiResponse(500, new ApiError(200,"error hahaha"));
        return apiResponse;
    }

    /**
     * 要求根据预报生产法郎
     * @param foreseenDTO
     * @return
     */
    @PostMapping("/foreseens")
    public ApiResponse foreseens(@RequestBody ForeseenDTO foreseenDTO){
        log.info("manager foreseen foreseen={}", foreseenDTO.toString());
        ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
        managerBalanceDTO.setBalanceId("11");
        managerBalanceDTO.setContractId("222");
        managerBalanceDTO.setContractNum(333);
        managerBalanceDTO.setCurrent(444.0D);
        managerBalanceDTO.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalanceDTO);
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

        ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
        managerBalanceDTO.setBalanceId("11");
        managerBalanceDTO.setContractId("222");
        managerBalanceDTO.setContractNum(333);
        managerBalanceDTO.setCurrent(444.0D);
        managerBalanceDTO.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalanceDTO);

 /*       apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/transactions")
    public ApiResponse transactions(@NotNull @RequestBody TransactionDTO transactionDTO){
        log.info("manager transaction transaction={}", transactionDTO.toString());

        ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
        managerBalanceDTO.setBalanceId("11");
        managerBalanceDTO.setContractId("222");
        managerBalanceDTO.setContractNum(333);
        managerBalanceDTO.setCurrent(444.0D);
        managerBalanceDTO.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalanceDTO);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/refills")
    public ApiResponse refills(@RequestBody RegistersDTO registersDTO){
        log.info("manager registers registers={}", registersDTO.toString());
        ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
        managerBalanceDTO.setBalanceId("11");
        managerBalanceDTO.setContractId("222");
        managerBalanceDTO.setContractNum(333);
        managerBalanceDTO.setCurrent(444.0D);
        managerBalanceDTO.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalanceDTO);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

    @PostMapping("/franking/stats")
    public ApiResponse stats(@RequestBody StatisticsDTO statisticsDTO){
        log.info("manager statistics statistics={}", statisticsDTO.toString());

        ManagerBalanceDTO managerBalanceDTO = new ManagerBalanceDTO();
        managerBalanceDTO.setBalanceId("11");
        managerBalanceDTO.setContractId("222");
        managerBalanceDTO.setContractNum(333);
        managerBalanceDTO.setCurrent(444.0D);
        managerBalanceDTO.setConsolidate(555.0D);
        ApiResponse apiResponse =  new ApiResponse(200, managerBalanceDTO);

  /*      apiResponse =  new ApiResponse(400, new OperationError());
        apiResponse =  new ApiResponse(500, new ApiError());*/
        return apiResponse;
    }

}
