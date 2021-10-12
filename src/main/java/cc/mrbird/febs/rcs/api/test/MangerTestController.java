package cc.mrbird.febs.rcs.api.test;


import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.rcs.api.ServiceInvokeRussia;
import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.PublicKeyGenerate;
import cc.mrbird.febs.rcs.dto.manager.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/test/managerTest")
public class MangerTestController {
    @Autowired
    ServiceInvokeRussia serviceInvokeRussia;

    String frankMachineId = "PM100500";
    String contractCode = "190eac0d-844c-11e5-90db-2c59e5453bd0";
    String userId = "11a8005e-6d6a-499d-9fca-82aa69103f90";
    String taxVersion = "2.3.3";
    String postOffice = "994700";
    double contractMaxCurrent = 3000.23;
    String productCode1 = "2100";
    String productCode2 = "2700";
    String foreseenId = "";

    int count1 = 10;
    double weight1 = 10;
    double amount1 = 100;

    /**
     *     String frankMachineId = "FM100002";
     *     String contractCode = "00001032";
     *     String contractId = "3aaeb112-ccb8-4312-ad2a-d50f9c91485a";
     *     String userId = "11a8005e-6d6a-499d-9fca-82aa69103f90";
     *     String taxVersion = "22.1.1";
     *     String postOffice = "994700";
     *     double contractMaxCurrent = 3000.23;
     *     String productCode1 = "2100";
     *     String productCode2 = "2101";
     *     String foreseenId = "";
     *
     *     int count1 = 10;
     *     double weight1 = 2;
     *     double amount1 = 25.0;
     */

    int count2 = 20;
    double weight2 = 10;
    double amount2 = count2 * weight2;

//    double totalAmount = amount1 + amount2;
//    double CreditVal = totalAmount;
//    int totalCount = count1 + count2;

    double totalAmount = amount1;
    int totalCount = count1;

    /**
     * 机器状况
     * http://localhost/p/test/managerTest/frankMachines
     */
    @GetMapping("frankMachines")
    public void frankMachines(){
        log.info("开始测试frankMachines");
        DeviceDTO bean = new DeviceDTO();
        bean.setId(frankMachineId);
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice(postOffice);
        bean.setTaxVersion(taxVersion);
        bean.setEvent(EventEnum.STATUS);

        log.info("frankMachines = {}", JSON.toJSONString(bean));

        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.frankMachines(bean);
        log.info("从manager服务器得到的数据{}", apiRussiaResponse.toString());
        /*switch (ResultEnum.getByCode(apiResponse.getCode())) {
            case SUCCESS:
                DeviceDTO deviceDTO = (DeviceDTO) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", deviceDTO.toString());
                break;
            case OPERATION_ERROR:
                OperationError operationError = (OperationError) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", operationError.toString());
                break;
            case INTERNAL_SERVICE_ERROR:
                ApiError apiError = (ApiError) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", apiError.toString());
                break;
        }*/
    }

    /**
     * 机器状况
     * http://localhost/p/test/managerTest/auth
     * http://localhost/p/test/managerTest/auth
     * http://192.168.30.60:89/pmTest/test/managerTest/auth
     */
    @GetMapping("auth")
    public void auth(){
        log.info("开始测试auth");
        //{"dateTime":"2021-09-28T13:16:54.000+08:00","event":"STATUS","id":"PM100500","postOffice":"131000","status":"ENABLED","taxVersion":"22.1.1"}
        DeviceDTO bean = new DeviceDTO();
        bean.setId("PM100500");
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice("131000");
        bean.setDateTime(DateKit.createRussiatime(new Date()));
        bean.setTaxVersion("22.1.1");
        bean.setEvent(EventEnum.STATUS);

        log.info("auth = {}", JSON.toJSONString(bean));

        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.auth(frankMachineId, bean);
        log.info("测试结束：object = " + apiRussiaResponse.getObject());
    }

    /**
     * 机器状况
     * http://localhost/p/test/managerTest/unauth
     */
    @GetMapping("unauth")
    public void unauth(){
        log.info("开始测试unauth");

        DeviceDTO bean = new DeviceDTO();
        bean.setId(frankMachineId);
        bean.setStatus(FMStatusEnum.UNAUTHORIZED);
        bean.setPostOffice(postOffice);
        bean.setTaxVersion(taxVersion);
        bean.setEvent(EventEnum.STATUS);

        log.info("unauth = {}", JSON.toJSONString(bean));
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.unauth(frankMachineId, bean);
        log.info("测试结束：object = " + apiRussiaResponse.getObject());
    }


    /**
     * 机器状况
     * http://localhost/p/test/managerTest/lost
     */
    @GetMapping("lost")
    public void lost(){
        log.info("开始测试 lost");

        DeviceDTO bean = new DeviceDTO();
        bean.setId(frankMachineId);
        bean.setStatus(FMStatusEnum.LOST);
        bean.setPostOffice(postOffice);
        bean.setTaxVersion(taxVersion);
        bean.setEvent(EventEnum.STATUS);

        log.info("lost = {}", JSON.toJSONString(bean));
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.lost(frankMachineId, bean);
        log.info("测试结束：object = " + apiRussiaResponse.getObject());
    }

    /**
     * 机器状况
     * http://auto.uprins.com/p/test/managerTest/publicKey
     * http://localhost/p/test/managerTest/publicKey
     */
    @GetMapping("publicKey")
    public void publicKey(){
        log.info("开始测试 publicKey");

        PublicKeyGenerate publicKeyGenerate = new PublicKeyGenerate();

        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
        publicKeyDTO.setKey("-----BEGIN PUBLIC KEY----- " + publicKeyGenerate.getPublicKey() + " -----END PUBLIC KEY-----");
        publicKeyDTO.setRevision(1);
        int expire = 365*3;
        publicKeyDTO.setExpireDate(DateKit.createRussiatime(DateKit.offsetDayDate(expire)));
        log.info("publicKeyDTO = {}", JSON.toJSONString(publicKeyDTO));
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.publicKey(frankMachineId, publicKeyDTO);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
    }

    /**
     * http://auto.uprins.com/p/test/managerTest/rateTables
     * http://localhost/p/test/managerTest/rateTables
     */
    @GetMapping("rateTables")
    public void rateTables(){
        log.info("开始测试 rateTables");

        RateTableFeedbackDTO rateTableFeedbackDTO = new RateTableFeedbackDTO();

        rateTableFeedbackDTO.setTaxVersion("22.1.0");
        rateTableFeedbackDTO.setStatus(true);
        rateTableFeedbackDTO.setRcsVersions(new String[]{"22.1.0"});
        rateTableFeedbackDTO.setTimestamp(DateKit.createRussiatime(new Date()));
        log.info("rateTableFeedbackDTO = {}", JSON.toJSONString(rateTableFeedbackDTO));
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.rateTables(rateTableFeedbackDTO);

        if (apiRussiaResponse != null) {
            log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
        }else{
            log.info("apiResponse is null ");
        }
    }

    /**
     * http://auto.uprins.com/p/test/managerTest/foreseens
     * http://localhost/p/test/managerTest/foreseens
     */
    @GetMapping("foreseens")
    public void foreseens(){
        log.info("开始测试 foreseens");
        String foreseenId = AESUtils.createUUID();
        this.foreseenId = foreseenId;


        ForeseenProductDTO foreseenProduct = new ForeseenProductDTO();
        foreseenProduct.setProductCode(productCode1);
        foreseenProduct.setCount(count1);
        foreseenProduct.setWeight(weight1);
        foreseenProduct.setAmount(amount1);


        /*ForeseenProductDTO foreseenProduct2 = new ForeseenProductDTO();
        foreseenProduct2.setProductCode(productCode2);
        foreseenProduct2.setCount(count2);
        foreseenProduct2.setWeight(weight2);
        foreseenProduct2.setAmount(amount2);*/


        ForeseenDTO foreseenDTO = new ForeseenDTO();
        foreseenDTO.setId(foreseenId);
        foreseenDTO.setPostOffice(postOffice);
        foreseenDTO.setUserId(userId);
//        foreseenDTO.setContractCode(contractCode);
        foreseenDTO.setContractId(contractCode);
        foreseenDTO.setTotalCount(totalCount);
//        foreseenDTO.setProducts(new ForeseenProductDTO[]{foreseenProduct, foreseenProduct2});
        foreseenDTO.setProducts(new ForeseenProductDTO[]{foreseenProduct});
        foreseenDTO.setFrankMachineId(frankMachineId);
        foreseenDTO.setTaxVersion(taxVersion);
        foreseenDTO.setTotalAmmount(totalAmount);

        log.info("foreseenId = {}", foreseenId);
        log.info("foreseen = {}", JSON.toJSONString(foreseenDTO));

        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.foreseens(foreseenDTO);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/cancel
     */
    @GetMapping("cancel")
    public void cancel(){
        log.info("开始测试 cancel");

        ForeseenCancel foreseenCancel = new ForeseenCancel("cancel job");
        log.info("foreseenCancel = {} foreseenId = {}", JSON.toJSONString(foreseenCancel), foreseenId);
        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.cancel(foreseenId, contractCode, foreseenCancel);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());

    }

    /**
     * http://localhost/p/test/managerTest/transactions
     */
    @GetMapping("transactions")
    public void transactions(){
        log.info("开始测试 transactions");

        String Prefix_4 = "!45!";
        String VersionOfCode_2 = "01";
        //生产厂家代码
        String ManufacturerCode_2 = "NE";
        String CountryCode_3 = "643";
        //受理邮局的唯一ID (邮政编码)
        String postCode_6 = postOffice;
        //邮寄的日期 241120 Format  (DDMMYY)
        String postDate_6 = new SimpleDateFormat("ddMMYY").format(new Date());
        log.info("postDate_6={}",postDate_6);
        //FM的SMPC注册号 200498
        String RegistrationNumber_6 = frankMachineId.substring(2);
        //邮资盖印前的注册“总数”值 13452039
        String totalCount_8 = String.format("%08d", count1);
        //金额 0002300
        String amount_7 = String.format("%07d", (int)amount1*100);
        //产品代码 2100
        String productType_4 = productCode1;
        //产品重量 实际重量 0020
        String productWeight_4 = String.valueOf((int)weight1*100);
        //客户的唯一Id(代码) 00150568
        String CustomerId_8 = "00001032";

        String dmMessage = Prefix_4 + VersionOfCode_2 + ManufacturerCode_2 + CountryCode_3 + postCode_6 + postDate_6 + RegistrationNumber_6 + totalCount_8 + amount_7 + productType_4 + productWeight_4 + CustomerId_8;
        log.info("dmMessage={}",dmMessage);

        FrankDTO frank = new FrankDTO();
        frank.setDmMessage("!45!01NE6434238001504211007130111638000026003100002200130941");
        frank.setDmMessage(dmMessage);

       /* FrankDTO frank2 = new FrankDTO();
        frank2.setDmMessage("message");*/

        String transactionId = AESUtils.createUUID();

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transactionId);
        transactionDTO.setForeseenId(foreseenId);
        transactionDTO.setPostOffice(postOffice);
        transactionDTO.setFrankMachineId(frankMachineId);
//        transactionDTO.setContractCode(contractCode);
        transactionDTO.setContractId(contractCode);
        transactionDTO.setStartDateTime(DateKit.createRussiatime(new Date()));
        transactionDTO.setStopDateTime(DateKit.createRussiatime(DateKit.offsetDayDate(1)));
        transactionDTO.setUserId(userId);
        /*transactionDTO.setCreditVal(amount1 + amount2);
        transactionDTO.setAmount(amount1 + amount2);
        transactionDTO.setCount(count1 + count2);*/
        transactionDTO.setCreditVal(totalAmount);
        transactionDTO.setAmount(totalAmount);
        transactionDTO.setCount(totalCount);
        transactionDTO.setGraphId("");
        transactionDTO.setTaxVersion(taxVersion);
//        transactionDTO.setFranks(new FrankDTO[]{frank , frank2});
        transactionDTO.setFranks(new FrankDTO[]{frank});
        log.info("transaction = {}", JSON.toJSONString(transactionDTO));

        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.transactions(transactionDTO);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/refills
     */
    @GetMapping("refills")
    @Deprecated
    public void refills(){
        log.info("开始测试 refills");

        RegistersDTO registersDTO = new RegistersDTO();
        registersDTO.setId("registers ID");
        registersDTO.setContractCode("");
        registersDTO.setContractNum(0);
        registersDTO.setPostofficeIndex("");
        registersDTO.setFrankMachineId("");
        registersDTO.setTimestamp("");
        registersDTO.setAscRegister(0.0D);
        registersDTO.setDecRegister(0.0D);
        registersDTO.setAmount(0.0D);
        registersDTO.setType("");


        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.refills(registersDTO);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/stats
     */
    @GetMapping("stats")
    @Deprecated
    public void stats(){
        log.info("开始测试 stats");
        TransactionDataDTO transactionDataDTO1 = new TransactionDataDTO();
        transactionDataDTO1.setDateTime("");
        transactionDataDTO1.setProductCode("");
        transactionDataDTO1.setWeight(0);
        transactionDataDTO1.setCount(0);
        transactionDataDTO1.setAmount(0.0D);
        transactionDataDTO1.setTaxVersion("");

        TransactionDataDTO transactionDataDTO2 = new TransactionDataDTO();
        transactionDataDTO2.setDateTime("");
        transactionDataDTO2.setProductCode("");
        transactionDataDTO2.setWeight(0);
        transactionDataDTO2.setCount(0);
        transactionDataDTO2.setAmount(0.0D);
        transactionDataDTO2.setTaxVersion("");

        FrankDTO frank = new FrankDTO();
        frank.setDmMessage("111");

        FrankDTO frank2 = new FrankDTO();
        frank2.setDmMessage("222");

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setId("statistics ID");
        statisticsDTO.setContractCode("");
        statisticsDTO.setContractNum(0);
        statisticsDTO.setPostOffice("");
        statisticsDTO.setFrankMachineId("");
        statisticsDTO.setFrankingPeriodStart("");
        statisticsDTO.setFrankingPeriodEnd("");
        statisticsDTO.setTotalAmount(0.0D);
        statisticsDTO.setInitialPieceCounter(0L);
        statisticsDTO.setFinalPieceCounter(0L);
        statisticsDTO.setTransactions(new TransactionDataDTO[]{transactionDataDTO1, transactionDataDTO2});
        statisticsDTO.setFranks(new FrankDTO[]{frank,frank2});


        ApiRussiaResponse apiRussiaResponse = serviceInvokeRussia.stats(statisticsDTO);
        log.info("测试结束：object = " + apiRussiaResponse.getObject().toString());
    }


}
