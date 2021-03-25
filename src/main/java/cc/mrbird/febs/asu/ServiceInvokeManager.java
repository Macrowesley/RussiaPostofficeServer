package cc.mrbird.febs.asu;
import cc.mrbird.febs.asu.entity.enums.Event;
import cc.mrbird.febs.asu.entity.enums.FMStatus;
import cc.mrbird.febs.asu.entity.manager.FMError;

import cc.mrbird.febs.asu.entity.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 调用俄罗斯服务器接口
 */
@Slf4j
@Component
public class ServiceInvokeManager {
//    @Autowired
//    RestTemplate restTemplate;

    private final String baseUrl = "http://40.114.247.228:8080/rcs-manager/v1";



    /**
     * 发送机器状况
     * @PutMapping("frankMachines")
     */
    public ApiResponse frankMachines(FrankMachine frankMachine) {

/*
        String url = "http://" + host + ":" + port + "/hello2";
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("name", name);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, map, String.class);
*/
        /*{
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        String url = "https://asufm.russianpost.ru/rcs-manager/v1/frankMachines";
//        url = "http://40.114.247.228:8080/rcs-manager/v1/frankMachines";

        FrankMachine machine = new FrankMachine();
        machine.setId("FM100001");
        machine.setDateTime("2021-01-01T09:00:00.001+03:00");
        machine.setStatus(FMStatus.ENABLED);
        machine.setPostOffice("131000");
        machine.setTaxVersion("A0042015A");
        machine.setEvent("STATUS");
        machine.setError(new FMError("200","ok"));


        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY","-----BEGIN PUBLIC KEY-----MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAHxZMuhGUvOwc6GKT6Y9V6+uSQmiLW9vCO4A1xy7qquqrNFmPlsQhPMZUZ62HBKDeH-----END PUBLIC KEY-----");

        HttpEntity<FrankMachine> httpEntity = new HttpEntity<>(machine, headers);

        ResponseEntity<String> entityResponseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);

        log.info(""+entityResponseEntity.getStatusCodeValue());
        log.info(entityResponseEntity.getHeaders().toString());
        log.info(entityResponseEntity.getBody().toString());


    }*/



        RestTemplate restTemplate = new RestTemplate();

//        String url = "https://auto.uprins.com/p/test/frankMachines";
        String url = "http://localhost/p/test/frankMachines";
        FrankMachine bean = new FrankMachine();
        bean.setId("");
        bean.setDateTime("");
        bean.setStatus(FMStatus.ENABLED);
        bean.setPostOffice("");
        bean.setTaxVersion("");
        //校验事件
        int eventType = 0;
        Event event = Event.getEventByType(eventType);
        if (event == null){
            //todo 待定
            return null;
        }
        bean.setEvent(event);
        bean.setError(new FMError("200","ok"));


        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, bean, String.class);

        log.info("结果：responseEntity = {}", responseEntity.toString());
        log.info("结果：responseEntity.getBody() = {}", responseEntity.getBody().toString());
        log.info("结果：responseEntity.getStatusCode() = {}", responseEntity.getStatusCode());
        log.info("结果：responseEntity.getStatusCodeValue() = {}", responseEntity.getStatusCodeValue());
        log.info("结果：responseEntity.getHeaders() = {}", responseEntity.getHeaders().toString());
        return null;
    }

    /**
     * FM授权请求
     * @PostMapping("/frankMachines/{frankMachineId}/auth")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public ApiResponse auth(String frankMachineId, FrankMachine frankMachine) {
        //TODO 处理远程api调用
        return null;
    }

    /**
     * 取消授权
     * @PostMapping("/frankMachines/{frankMachineId}/unauth")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public ApiResponse unauth(String frankMachineId, FrankMachine frankMachine) {
        return null;
    }


    /**
     * 取消授权
     * @PostMapping("/frankMachines/{frankMachineId}/lost")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public ApiResponse lost(String frankMachineId, FrankMachine frankMachine) {
        return null;
    }

    /**
     * 提交新的加密密钥
     * @PutMapping("/frankMachines/{frankMachineId}/publicKey")
     * @param frankMachineId
     * @param PublicKey
     * @return
     */
    public ApiResponse publicKey(String frankMachineId, PublicKey PublicKey) {
        return null;
    }

    /**
     * 资费表加载结果
     * @PutMapping("/rateTables")
     * @param rateTableFeedback
     * @return
     */
    public ApiResponse rateTables(RateTableFeedback rateTableFeedback) {
        return null;
    }

    /**
     * 要求根据预报生产法郎
     * @PostMapping("/foreseens")
     * @param foreseen
     * @return
     */
    public ApiResponse foreseens(Foreseen foreseen) {
        return null;
    }

    /**
     * 取消工作任务
     * @PostMapping("/foreseens/{foreseenId}/cancel")
     * @param foreseenId
     * @param foreseenCancel
     * @return
     */
    public ApiResponse cancel(String foreseenId, ForeseenCancel foreseenCancel) {
        return null;
    }

    /**
     * @PostMapping("/transactions")
     * @param transaction
     */
    public ApiResponse transactions(Transaction transaction) {
        return null;
    }

    /**
     * @PostMapping("/refills")
     * @param registers
     */
    public ApiResponse refills(Registers registers) {
        return null;
    }

    /**
     * @PostMapping("/franking/stats")
     * @param statistics
     */
    public ApiResponse stats(Statistics statistics) {
        return null;
    }


}
