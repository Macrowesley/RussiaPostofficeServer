package cc.mrbird.febs.asu;
import cc.mrbird.febs.asu.entity.enums.FMStatus;
import cc.mrbird.febs.asu.entity.manager.FMError;

import cc.mrbird.febs.asu.entity.manager.*;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;

/**
 * 调用俄罗斯服务器接口
 */
@Slf4j
@Component
public class ServiceInvokeManager {
    @Autowired
    RestTemplate restTemplate;

    private final String baseUrl = "https://http://40.114.247.228:8080/rcs-manager/v1";

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    /**
     * 发送机器状况
     * @PutMapping("frankMachines")
     */
    public void frankMachines(FrankMachine frankMachine) {

/*
        String url = "http://" + host + ":" + port + "/hello2";
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("name", name);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, map, String.class);
*/
        /*RestTemplate restTemplate = new RestTemplate();

        String url = baseUrl + "/frankMachines";
        FrankMachine bean = new FrankMachine();
        bean.setId("");
        bean.setDateTime("");
        bean.setStatus(FMStatus.ENABLED);
        bean.setPostOffice("");
        bean.setTaxVersion("");
        bean.setEvent("");
        bean.setError(new FMError("200","ok"));


        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, bean, String.class);

        log.info("responseEntity = {}", responseEntity.toString());
        log.info("responseEntity.getBody() = {}", responseEntity.getBody().toString());
        log.info("responseEntity.getStatusCode() = {}", responseEntity.getStatusCode());
        log.info("responseEntity.getStatusCodeValue() = {}", responseEntity.getStatusCodeValue());
        log.info("responseEntity.getHeaders() = {}", responseEntity.getHeaders().toString());*/



        RestTemplate restTemplate = new RestTemplate();

        String url = "https://auto.uprins.com/p/test/frankMachines";
        FrankMachine bean = new FrankMachine();
        bean.setId("");
        bean.setDateTime("");
        bean.setStatus(FMStatus.ENABLED);
        bean.setPostOffice("");
        bean.setTaxVersion("");
        bean.setEvent("");
        bean.setError(new FMError("200","ok"));


        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, bean, String.class);

        log.info("responseEntity = {}", responseEntity.toString());
        log.info("responseEntity.getBody() = {}", responseEntity.getBody().toString());
        log.info("responseEntity.getStatusCode() = {}", responseEntity.getStatusCode());
        log.info("responseEntity.getStatusCodeValue() = {}", responseEntity.getStatusCodeValue());
        log.info("responseEntity.getHeaders() = {}", responseEntity.getHeaders().toString());
    }

    /**
     * FM授权请求
     * @PostMapping("/frankMachines/{frankMachineId}/auth")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public void auth(String frankMachineId, FrankMachine frankMachine) {





    }

    /**
     * 取消授权
     * @PostMapping("/frankMachines/{frankMachineId}/unauth")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public void unauth(String frankMachineId, FrankMachine frankMachine) {

    }


    /**
     * 取消授权
     * @PostMapping("/frankMachines/{frankMachineId}/lost")
     * @param frankMachineId
     * @param frankMachine
     * @return
     */
    public void lost(String frankMachineId, FrankMachine frankMachine) {

    }

    /**
     * 提交新的加密密钥
     * @PutMapping("/frankMachines/{frankMachineId}/publicKey")
     * @param frankMachineId
     * @param PublicKey
     * @return
     */
    public void publicKey(String frankMachineId, PublicKey PublicKey) {

    }

    /**
     * 资费表加载结果
     * @PutMapping("/rateTables")
     * @param rateTableFeedback
     * @return
     */
    public void rateTables(RateTableFeedback rateTableFeedback) {

    }

    /**
     * 要求根据预报生产法郎
     * @PostMapping("/foreseens")
     * @param foreseen
     * @return
     */
    public void foreseens(Foreseen foreseen) {

    }

    /**
     * 取消工作任务
     * @PostMapping("/foreseens/{foreseenId}/cancel")
     * @param foreseenId
     * @param foreseenCancel
     * @return
     */
    public void cancel(String foreseenId, ForeseenCancel foreseenCancel) {

    }

    /**
     * @PostMapping("/transactions")
     * @param transaction
     */
    public void transactions(Transaction transaction) {

    }

    /**
     * @PostMapping("/refills")
     * @param registers
     */
    public void refills(Registers registers) {

    }

    /**
     * @PostMapping("/franking/stats")
     * @param statistics
     */
    public void stats(Statistics statistics) {

    }


}
