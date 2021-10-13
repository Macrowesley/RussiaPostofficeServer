package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.dto.manager.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
//	 private final String baseUrl = "http://test.asufm-test.10.238.33.32.nip.io/rcs-manager";
    private final String baseUrl = "http://rcs-manager.asufm-test.10.193.86.137.nip.io";
    private final String testContractCode = "190eac0d-844c-11e5-90db-2c59e5453bd0";
    /**
     * 表头号     FMID          合同id
     * MXX001 PM100500 00001032
     * CPU123 FM100002  00001033
     * 端口号：12800
     */
    private final boolean isTest = false;
    /**
     * 发送机器状况
     *
     * @PutMapping("frankMachines")
     */
    public ApiRussiaResponse frankMachines(DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),deviceDTO);
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
    public ApiRussiaResponse auth(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }

        String url = baseUrl + "/frankMachines/{frankMachineId}/auth";
        HashMap<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        DeviceDTO data = new DeviceDTO();
        data.setId(deviceDTO.getId());
        data.setDateTime(deviceDTO.getDateTime());
        data.setPostOffice(deviceDTO.getPostOffice());
        return doExchange(url, data, HttpMethod.POST, String.class, map);
    }

    /**
     * 取消授权
     *
     * @param frankMachineId
     * @param deviceDTO
     * @return
     * @PostMapping("/frankMachines/{frankMachineId}/unauth")
     */
    public ApiRussiaResponse unauth(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),"ok");
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
    public ApiRussiaResponse lost(String frankMachineId, DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),"ok");
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
    public ApiRussiaResponse publicKey(String frankMachineId, PublicKeyDTO publicKeyDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),"ok");
        }


        String url = baseUrl + "/frankMachines/{frankMachineId}/publicKey";

        Map<String, String> map = new HashMap<>();
        map.put("frankMachineId", frankMachineId);

        return doExchange(url, publicKeyDTO, HttpMethod.PUT, String.class, map);
    }

    /**
     * 通过该接口发送机器税率信息
     * 场景有以下2种：
     * 1. 机器完成auth接口，成功后，更新税率表后，调用此接口
     * 2. 机器更新税率信息后，更新税率表
     * 其实都是开机启动的时候，需要判断机器是否更新税率表
     {
         "id": "PM100500",
         "dateTime": "2021-01-01T09:00:00.001+03:00",
         "status": "BLOCKED",
         "postOffice": "131000",
         "taxVersion": "22.1.1",
         "event": "RATE_TABLE_UPDATE",
         "error": {}
     }
     */
    public ApiRussiaResponse frankMachinesRateTableUpdateEvent(DeviceDTO deviceDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),deviceDTO);
        }

        String url = baseUrl + "/frankMachines";
        return doExchange(url, deviceDTO, HttpMethod.PUT, DeviceDTO.class,null);
    }

    /**
     * 告知俄罗斯税率表加载ok
     * 场景如下：
     * 服务器收到俄罗斯给的税率表后，处理成功后，再发送给俄罗斯
     *
     * @param rateTableFeedbackDTO
     * @return
     * @PutMapping("/rateTables")
     */
//    @Async(value = FebsConstant.ASYNC_POOL)
    public ApiRussiaResponse rateTables(RateTableFeedbackDTO rateTableFeedbackDTO) {
        //测试条件下，返回假数据
        if (isTest){
            return  new ApiRussiaResponse(ResultEnum.SUCCESS.getCode(),"ok");
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
    public ApiRussiaResponse foreseens(ForeseenDTO foreseenDTO) {
        //测试条件下，返回假数据
        if (isTest){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(foreseenDTO.getContractCode());
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiRussiaResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
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
    public ApiRussiaResponse cancel(String foreseenId, String contractCode, ForeseenCancel foreseenCancel) {
        //测试条件下，返回假数据
        if (isTest){
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(contractCode);
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiRussiaResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
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
    public ApiRussiaResponse transactions(TransactionDTO transactionDTO) {
        //测试条件下，返回假数据
        if (isTest){
            ManagerBalanceDTO balanceDTO = new ManagerBalanceDTO();
            balanceDTO.setContractCode(transactionDTO.getContractCode());
            balanceDTO.setCurrent(100000D);
            balanceDTO.setConsolidate(100000D);
            return new ApiRussiaResponse(ResultEnum.SUCCESS.getCode() ,balanceDTO);
        }

        String url = baseUrl + "/transactions";
        return doExchange(url, transactionDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }

    /**
     * @param registersDTO
     * @PostMapping("/refills")
     */
    @Deprecated
    public ApiRussiaResponse refills(RegistersDTO registersDTO) {
        String url = baseUrl + "/refills";
        return doExchange(url, registersDTO, HttpMethod.POST, ManagerBalanceDTO.class,null);
    }

    /**
     * @param statisticsDTO
     * @PostMapping("/franking/stats")
     */
    @Deprecated
    public ApiRussiaResponse stats(StatisticsDTO statisticsDTO) {
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
    private <T, E> ApiRussiaResponse doExchange(String url, E requestBody, HttpMethod method, Class<T> responseObjectClass, Map<String, ?> uriVariables) {
        log.info("给manager服务器发送消息：url = {}, 内容={}", url, JSON.toJSONString(requestBody));
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
                return new ApiRussiaResponse(statusCodeValue,"ok");
            }
            return new ApiRussiaResponse(statusCodeValue,responseEntity.getBody());
        } catch (HttpClientErrorException e){
//            e.printStackTrace();
            log.info("HttpClientErrorException error = " + e.getMessage());
            log.info("HttpClientErrorException StatusCode={}, ResponseBody={}",  e.getRawStatusCode(), e.getResponseBodyAsString());
            OperationError operationError = JSONObject.parseObject(e.getResponseBodyAsString(), OperationError.class);
            return new ApiRussiaResponse(e.getRawStatusCode(), operationError);
        } catch (HttpServerErrorException.InternalServerError e) {
//            e.printStackTrace();
            log.info("InternalServerError StatusCode={}, ResponseBody={}",  e.getRawStatusCode(), e.getResponseBodyAsString());
            ApiError apiError = JSONObject.parseObject(e.getResponseBodyAsString(), ApiError.class);
            return new ApiRussiaResponse(e.getRawStatusCode(), apiError);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception信息：" + e.getMessage());
            return new ApiRussiaResponse(ResultEnum.UNKNOW_ERROR.getCode(), "网络问题，无法收到返回值2");
        }
    }
}
