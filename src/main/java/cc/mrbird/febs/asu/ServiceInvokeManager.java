package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.entity.enums.ResultEnum;
import cc.mrbird.febs.asu.entity.manager.*;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
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
    private final String baseUrl = "http://localhost/p/test/manager";

    /**
     * 发送机器状况
     *
     * @PutMapping("frankMachines")
     */
    public ApiResponse frankMachines(FrankMachine frankMachine) {
        String url = baseUrl + "/frankMachines";
        return doExchange(url, frankMachine, HttpMethod.PUT, FrankMachine.class,null);
    }


    /**
     * FM授权请求
     *
     * @param frankMachineId
     * @param frankMachine
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/auth")
     */
    public ApiResponse auth(String frankMachineId, FrankMachine frankMachine) {
        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";
        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, frankMachine, HttpMethod.POST, String.class, map);

    }

    /**
     * 取消授权
     *
     * @param frankMachineId
     * @param frankMachine
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/unauth")
     */
    public ApiResponse unauth(String frankMachineId, FrankMachine frankMachine) {
        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";

        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, frankMachine, HttpMethod.POST, String.class, map);
    }


    /**
     * 取消授权
     *
     * @param frankMachineId
     * @param frankMachine
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/lost")
     */
    public ApiResponse lost(String frankMachineId, FrankMachine frankMachine) {
        String url = baseUrl + "/frankMachines/{frankMachineId}/lost";

        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, frankMachine, HttpMethod.POST, String.class, map);
    }

    /**
     * 提交新的加密密钥
     *
     * @param frankMachineId
     * @param PublicKey
     * @return
     * @PutMapping("/frankMachines/{frankMachineId}/publicKey")
     */
    public ApiResponse publicKey(String frankMachineId, PublicKey publicKey) {
        String url = baseUrl + "/frankMachines/{frankMachineId}/publicKey";

        Map<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);
        return doExchange(url, publicKey, HttpMethod.POST, String.class, map);
    }

    /**
     * 资费表加载结果
     *
     * @param rateTableFeedback
     * @return
     * @PutMapping("/rateTables")
     */
    public ApiResponse rateTables(RateTableFeedback rateTableFeedback) {
        String url = baseUrl + "/rateTables";
        return doExchange(url, rateTableFeedback, HttpMethod.PUT, String.class,null);
    }

    /**
     * 要求根据预报生产法郎
     *
     * @param foreseen
     * @return
     * @PostMapping("/foreseens")
     */
    public ApiResponse foreseens(Foreseen foreseen) {
        String url = baseUrl + "/foreseens";
        return doExchange(url, foreseen, HttpMethod.POST, ManagerBalance.class,null);
    }

    /**
     * 取消工作任务
     *
     * @param foreseenId
     * @param foreseenCancel
     * @return
     * @PostMapping("/foreseens/{foreseenId}/cancel")
     */
    public ApiResponse cancel(String foreseenId, ForeseenCancel foreseenCancel) {
        String url = baseUrl + "/foreseens/{foreseenId}/cancel";

        Map<String, String> map = new HashMap<>();
        map.put("foreseenId", foreseenId);

        return doExchange(url, foreseenCancel, HttpMethod.POST, ManagerBalance.class, map);
    }

    /**
     * @param transaction
     * @PostMapping("/transactions")
     */
    public ApiResponse transactions(Transaction transaction) {
        String url = baseUrl + "/transactions";
        return doExchange(url, transaction, HttpMethod.POST, ManagerBalance.class,null);
    }

    /**
     * @param registers
     * @PostMapping("/refills")
     */
    public ApiResponse refills(Registers registers) {
        String url = baseUrl + "/refills";
        return doExchange(url, registers, HttpMethod.POST, ManagerBalance.class,null);
    }

    /**
     * @param statistics
     * @PostMapping("/franking/stats")
     */
    public ApiResponse stats(Statistics statistics) {
        String url = baseUrl + "/franking/stats";
        return doExchange(url, statistics, HttpMethod.POST, ManagerBalance.class,null);
    }


    /**
     * 获取httpHeader信息
     *
     * @return
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        //todo 处理秘钥
        httpHeaders.add("X-API-KEY", "myKeyIsNotNull");
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
            if (uriVariables == null){
                responseEntity = restTemplate.exchange(url, method, requestEntity, ApiResponse.class);
            }else{
                responseEntity = restTemplate.exchange(url, method, requestEntity, ApiResponse.class, uriVariables);
            }

            if (ResultEnum.SUCCESS.getCode() == responseEntity.getStatusCodeValue()) {
                ApiResponse apiResponse = responseEntity.getBody();
                log.info("manager返回的结果 ApiResponse = {}", apiResponse.toString());
                switch (ResultEnum.getByCode(apiResponse.getCode())) {
                    case SUCCESS:
                        T bean = JSONObject.parseObject(JSONObject.toJSONString(apiResponse.getObject()), responseObjectClass);
                        log.info("manager返回的结果 {} = {}", bean.getClass().getTypeName(), bean.toString());
                        apiResponse.setObject(bean);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ApiResponse(ResultEnum.UNKNOW_ERROR.getCode(), "网络问题，无法收到返回值2");
        }
    }


}
