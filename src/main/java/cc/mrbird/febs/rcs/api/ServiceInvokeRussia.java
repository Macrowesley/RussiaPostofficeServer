package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.dto.manager.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用俄罗斯服务器接口俄罗斯
 */
@Slf4j
@Component
public class ServiceInvokeRussia {
    @Autowired
    RestTemplate restTemplate;

//    private final String baseUrl = "http://40.114.247.228:8080/rcs-manager/v1";
//    private final String baseUrl = "https://asufm.russianpost.ru/rcs-manager/v1";
    //最新
	 private final String baseUrl = "http://test.asufm-test.10.238.33.32.nip.io/rcs-manager";
//    private final String baseUrl = "http://localhost/p/test/manager";
    private final String testContractCode = "190eac0d-844c-11e5-90db-2c59e5453bd0";
    /**
     * 表头号     FMID          合同id
     * MXX001 PM100500 00001032
     * CPU123 FM100002  00001033
     * 端口号：12800
     */
    private final boolean isTest = true;
    /**
     * 发送机器状况
     *
     * @PutMapping("frankMachines")
     */
    public ApiResponse frankMachines(DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),deviceDTO);
        }

        String url = baseUrl + "/frankMachines";
        return doExchange(url, deviceDTO, HttpMethod.PUT, DeviceDTO.class,null);
    }


    /**
     * FM授权请求
     *
     * @param frankMachineId
     * @param deviceDTO
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/auth")
     */
    public ApiResponse auth(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }

        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";
        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, deviceDTO, HttpMethod.POST, String.class, map);
    }

    /**
     * 取消授权
     *
     * @param frankMachineId
     * @param deviceDTO
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/unauth")
     */
    public ApiResponse unauth(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }


        String url = baseUrl + "/frankMachines/{frankMachineId}/unauth";

        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, deviceDTO, HttpMethod.POST, String.class, map);
    }


    /**
     * 取消授权
     *
     * @param frankMachineId
     * @param deviceDTO
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/lost")
     */
    public ApiResponse lost(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }

        String url = baseUrl + "/frankMachines/{frankMachineId}/lost";

        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, deviceDTO, HttpMethod.POST, String.class, map);
    }

    /**
     * 提交新的加密密钥
     *
     * @param frankMachineId
     * @param PublicKey
     * @return
     * @PutMapping("/frankMachines/{frankMachineId}/publicKey")
     */
    public ApiResponse publicKey(String frankMachineId, PublicKeyDTO publicKeyDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }


        String url = baseUrl + "/frankMachines/{frankMachineId}/publicKey";

        Map<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, publicKeyDTO, HttpMethod.PUT, String.class, map);
    }

    /**
     * todo 待定资费表加载结果
     *
     * @param rateTableFeedbackDTO
     * @return
     * @PutMapping("/rateTables")
     */
//    @Async(value = FebsConstant.ASYNC_POOL)
    public ApiResponse rateTables(RateTableFeedbackDTO rateTableFeedbackDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }

        String url = baseUrl + "/rateTables";
        return doExchange(url, rateTableFeedbackDTO, HttpMethod.PUT, String.class,null);
    }

    /**
     * 要求根据预报生产法郎
     *
     * @param foreseenDTO
     * @return
     * @PostMapping("/foreseens")
     */
    public ApiResponse foreseens(ForeseenDTO foreseenDTO) {
        //测试条件下，返回假数据
        if (isTest){
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(foreseenDTO.getContractCode());
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
        }

        String url = baseUrl + "/foreseens";
        return doExchange(url, foreseenDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }

    /**
     * 取消工作任务
     *
     * @param foreseenId
     * @param foreseenCancel
     * @return
     * @PostMapping("/foreseens/{foreseenId}/cancel")
     */
    public ApiResponse cancel(String foreseenId, String contractCode, ForeseenCancel foreseenCancel) {
        //测试条件下，返回假数据
        if (isTest){
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(contractCode);
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
        }

        String url = baseUrl + "/foreseens/{foreseenId}/cancel";

        Map<String, String> map = new HashMap<>();
        map.put("foreseenId", foreseenId);

        return doExchange(url, foreseenCancel, HttpMethod.POST, ManagerBalanceDTO.class, map);
    }

    /**
     * @param transactionDTO
     * @PostMapping("/transactions")
     */
    public ApiResponse transactions(TransactionDTO transactionDTO) {
        //测试条件下，返回假数据
        if (isTest){
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(transactionDTO.getContractCode());
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
        }

        String url = baseUrl + "/transactions";
        return doExchange(url, transactionDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }

    /**
     * @param registersDTO
     * @PostMapping("/refills")
     */
    @Deprecated
    public ApiResponse refills(RegistersDTO registersDTO) {
        String url = baseUrl + "/refills";
        return doExchange(url, registersDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }

    /**
     * @param statisticsDTO
     * @PostMapping("/franking/stats")
     */
    @Deprecated
    public ApiResponse stats(StatisticsDTO statisticsDTO) {
        String url = baseUrl + "/franking/stats";
        return doExchange(url, statisticsDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }


    /**
     * 获取httpHeader信息
     *
     * @return
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        //没有头部信息，暂时忽略
        httpHeaders.add("X-API-KEY", "postmartUat");
        return httpHeaders;
    }

    /**
     * 处理远程调用接口
     * @param url 路径
     * @param requestBody 发送的对象
     * @param method 方法
     * @param responseObjectClass 返回的数据类型
     * @param uriVariables 链接中带的参数
     * @param <T> 返回的数据类型
     * @param <E> 发送的数据类型
     * @return
     */
    private <T, E> ApiResponse doExchange(String url, E requestBody, HttpMethod method, Class<T> responseObjectClass, Map<String, ?> uriVariables) {
        log.info("给manager服务器发送消息：url = {}, 内容={}", url, requestBody.toString());
        try {
            HttpEntity<E> requestEntity = new HttpEntity<>(requestBody, getHttpHeaders());
//            HttpEntity<E> requestEntity = new HttpEntity<>(requestBody);
            ResponseEntity<T> responseEntity;
            if (uriVariables == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, responseObjectClass);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, responseObjectClass, uriVariables);
            }

            log.info("responseEntity = " + responseEntity.toString());
            log.info("code={}, header={}, body={}", responseEntity.getStatusCodeValue(), responseEntity.getHeaders().toString(), responseEntity.getBody());

            int statusCodeValue = responseEntity.getStatusCodeValue();
            if (responseEntity.getBody() == null){
                return new ApiResponse(statusCodeValue,"ok");
            }
            return new ApiResponse(statusCodeValue,responseEntity.getBody());
        } catch (HttpClientErrorException e){
//            e.printStackTrace();
            log.info("HttpClientErrorException error = " + e.getMessage());
            log.info("HttpClientErrorException StatusCode={}, ResponseBody={}",  e.getRawStatusCode(), e.getResponseBodyAsString());
            OperationError operationError = JSONObject.parseObject(e.getResponseBodyAsString(), OperationError.class);
            return new ApiResponse(e.getRawStatusCode(), operationError);
        } catch (HttpServerErrorException.InternalServerError e) {
//            e.printStackTrace();
            log.info("InternalServerError StatusCode={}, ResponseBody={}",  e.getRawStatusCode(), e.getResponseBodyAsString());
            ApiError apiError = JSONObject.parseObject(e.getResponseBodyAsString(), ApiError.class);
            return new ApiResponse(e.getRawStatusCode(), apiError);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception信息：" + e.getMessage());
            return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "网络问题，无法收到返回值2");
        }
    }
}
