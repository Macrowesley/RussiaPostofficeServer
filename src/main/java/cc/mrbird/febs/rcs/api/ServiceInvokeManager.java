package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.*;
import com.alibaba.fastjson.JSON;
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
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用俄罗斯服务器接口
 */
@Slf4j
@Component
public class ServiceInvokeManager {
    @Autowired
    RestTemplate restTemplate;

//    private final String baseUrl = "http://40.114.247.228:8080/rcs-manager/v1";
//    private final String baseUrl = "https://asufm.russianpost.ru/rcs-manager/v1";
    //最新
	 private final String baseUrl = "http://test.asufm-test.10.238.33.32.nip.io/rcs-manager";
//    private final String baseUrl = "http://localhost/p/test/manager";
    private final String testContractId = "111-aaa-333-bbb-555-666";

    /**
     * 发送机器状况
     *
     * @PutMapping("frankMachines")
     */
    public ApiResponse frankMachines(DeviceDTO deviceDTO) {
        String url = baseUrl + "/frankMachines";
        return doExchange(url, deviceDTO, HttpMethod.PUT, String.class,null);
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
        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";
        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        /*if (frankMachineId.equals("FM100001")){
            return new ApiResponse(ResultEnum.SUCCESS.getCode(), "ok");
        }*/

        return doExchange(url, deviceDTO, HttpMethod.POST, String.class, map);

        //测试代码
   /*     frankMachineId = "FM100001";
        frankMachineId = "NE100700";
        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";
        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        DeviceTest deviceTest = new DeviceTest();
        deviceTest.setId(frankMachineId);


        *//*deviceTest.setId(frankMachineId);
        deviceTest.setPostOffice("131000");
        deviceTest.setDateTime(DateKit.createRussiatime());
        deviceTest.setStatus("ENABLED");
        deviceTest.setTaxVersion("1.0");*//*

//        deviceTest.setPostOffice("394040");
        deviceTest.setPostOffice("994700");
        deviceTest.setDateTime(DateKit.createRussiatime());
        deviceTest.setStatus("ENABLED");
//        deviceTest.setTaxVersion("1.0");
        deviceTest.setTaxVersion("2.3.3");

        log.info(JSON.toJSONString(deviceTest));

        return doExchange(url, deviceTest, HttpMethod.POST, String.class, map);*/
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
     * @PostMapping("/frankMachines/{frankMachineId}/lost")
     */
    public ApiResponse lost(String frankMachineId, DeviceDTO deviceDTO) {
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
        String url = baseUrl + "/frankMachines/{frankMachineId}/publicKey";

        Map<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);
        return doExchange(url, publicKeyDTO, HttpMethod.POST, String.class, map);
    }

    /**
     * todo 待定资费表加载结果
     *
     * @param rateTableFeedbackDTO
     * @return
     * @PutMapping("/rateTables")
     */
    @Async(value = FebsConstant.ASYNC_POOL)
    public ApiResponse rateTables(RateTableFeedbackDTO rateTableFeedbackDTO) {
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
        //todo 当看到特殊合同号，返回模拟结果
       /* if (foreseenDTO.getContractId().equals(testContractId)){
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,"ok");
        }*/
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
    public ApiResponse cancel(String foreseenId, String contractId, ForeseenCancel foreseenCancel) {
        //todo 当看到特殊合同号，返回模拟结果
        if (contractId.equals(testContractId)){
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,"ok");
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
        //todo 当看到特殊合同号，返回模拟结果
        if (transactionDTO.getContractId().equals(testContractId)){
            return new ApiResponse(ResultEnum.SUCCESS.getCode() ,"ok");
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
        //todo 处理秘钥
        httpHeaders.add("X-API-KEY", "-----BEGIN PUBLIC KEY-----\n" +
                "      MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAHxZMuhGUvOwc6GKT6Y9V6+uSQmiLW\n" +
                "      9vCO4A1xy7qquqrNFmPlsQhPMZUZ62HBKDeH\n" +
                "      -----END PUBLIC KEY-----");
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
        log.info("给manager服务器发送消息：{}", requestBody.toString());
        try {
            HttpEntity<E> requestEntity = new HttpEntity<>(requestBody, getHttpHeaders());
            ResponseEntity<ApiResponse> responseEntity;
            if (uriVariables == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, ApiResponse.class);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, ApiResponse.class, uriVariables);
            }
            log.info("responseEntity = " + responseEntity.toString());
            log.info("code={}, header={}, body={}", responseEntity.getStatusCodeValue(), responseEntity.getHeaders().toString(), responseEntity.getBody());

            int statusCodeValue = responseEntity.getStatusCodeValue();

            return getApiResponse(responseObjectClass, statusCodeValue, responseEntity.getBody());

            /*if (ResultEnum.SUCCESS.getCode() == statusCodeValue || ResultEnum.SUCCESS_204.getCode() == statusCodeValue) {
                ApiResponse apiResponse = responseEntity.getBody();
                log.info("manager返回的结果 ApiResponse = {}", apiResponse.toString());
                switch (ResultEnum.getByCode(apiResponse.getCode())) {
                    case SUCCESS:
                        T bean = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), responseObjectClass);
                        log.info("manager返回200结果 {} = {}", bean.getClass().getTypeName(), bean.toString());
                        apiResponse.setObject(bean);
                        return apiResponse;
                    case SUCCESS_204:
                        log.info("manager返回204结果 OK");
                        apiResponse.setObject("OK");
                        return apiResponse;
                    case OPERATION_ERROR:
                        OperationError operationError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), OperationError.class);
                        log.info("manager返回的结果 operationError = {}", operationError.toString());
                        apiResponse.setObject(operationError);
                        return apiResponse;
                    case INTERNAL_SERVICE_ERROR:
                        ApiError apiError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), ApiError.class);
                        log.info("manager返回的结果 apiError = {}", apiError.toString());
                        apiResponse.setObject(apiError);
                        return apiResponse;
                    default:
                        return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "状态码不对");
                }
            } else {
                log.info("网络问题，错误代码{} ", responseEntity.getStatusCodeValue());
                return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "网络问题，无法收到返回值1");
            }*/

        } catch (HttpClientErrorException e){
            return getApiResponse(responseObjectClass, e.getRawStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "网络问题，无法收到返回值2");
        }
    }

    private <T> ApiResponse getApiResponse(Class<T> responseObjectClass, int statusCodeValue, Object object ) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(statusCodeValue);
        apiResponse.setObject(object);

        /*switch (ResultEnum.getByCode(statusCodeValue)) {
            case SUCCESS:
                T bean = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), responseObjectClass);
                log.info("manager返回200结果 {} = {}", bean.getClass().getTypeName(), bean.toString());
                apiResponse.setObject(bean);
                return apiResponse;
            case SUCCESS_204:
                log.info("manager返回204结果 OK");
                apiResponse.setObject("OK");
                return apiResponse;
            case OPERATION_ERROR:
                OperationError operationError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), OperationError.class);
                log.info("manager返回{}结果 operationError = {}", statusCodeValue, operationError.toString());
                apiResponse.setObject(operationError);
                return apiResponse;
            case INTERNAL_SERVICE_ERROR:
                ApiError apiError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), ApiError.class);
                log.info("manager返回500结果 apiError = {}", apiError.toString());
                apiResponse.setObject(apiError);
                return apiResponse;
            default:
                return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "状态码不对");
        }*/

        if (statusCodeValue == ResultEnum.SUCCESS.getCode()){
//            T bean = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), responseObjectClass);
            T bean = JSONObject.parseObject(apiResponse.getObject().toString(), responseObjectClass);
            log.info("manager返回200结果 {} = {}", bean.getClass().getTypeName(), bean.toString());
            apiResponse.setObject(bean);
            return apiResponse;
        } else if (statusCodeValue == ResultEnum.SUCCESS_204.getCode()){
            log.info("manager返回204结果 OK");
            apiResponse.setObject("OK");
            return apiResponse;
        }else if (statusCodeValue >= 400 && statusCodeValue < 500){
//            OperationError operationError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), OperationError.class);
            OperationError operationError = JSONObject.parseObject(apiResponse.getObject().toString(), OperationError.class);
            log.info("manager返回{}结果 operationError = {}", statusCodeValue, operationError.toString());
            apiResponse.setObject(operationError);
            return apiResponse;
        } else if(statusCodeValue == ResultEnum.INTERNAL_SERVICE_ERROR.getCode()){
//            ApiError apiError = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), ApiError.class);
            ApiError apiError = JSONObject.parseObject(apiResponse.getObject().toString(), ApiError.class);
            log.info("manager返回500结果 apiError = {}", apiError.toString());
            apiResponse.setObject(apiError);
            return apiResponse;
        }else{
            return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "状态码不对");
        }
    }


}
