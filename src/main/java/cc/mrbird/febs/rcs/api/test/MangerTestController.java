package cc.mrbird.febs.rcs.api.test;


import cc.mrbird.febs.rcs.api.ServiceInvokeManager;
import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.dto.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("/test/managerTest")
public class MangerTestController {
    @Autowired
    ServiceInvokeManager serviceInvokeManager;
    /**
     * 机器状况
     * http://localhost/p/test/managerTest/frankMachines
     */
    @GetMapping("frankMachines")
    public void frankMachines(){
        log.info("开始测试frankMachines");
        FrankMachineDTO bean = new FrankMachineDTO();
        bean.setId("FM100001");
        bean.setDateTime("2");
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice("3");
        bean.setTaxVersion("4");
        bean.setEventEnum(EventEnum.STATUS);
        bean.setError(new FMError("200", "ok"));
        ApiResponse apiResponse = serviceInvokeManager.frankMachines(bean);

        switch (ResultEnum.getByCode(apiResponse.getCode())) {
            case SUCCESS:
                FrankMachineDTO frankMachineDTO = (FrankMachineDTO) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", frankMachineDTO.toString());
                break;
            case OPERATION_ERROR:
                OperationError operationError = (OperationError) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", operationError.toString());
                break;
            case INTERNAL_SERVICE_ERROR:
                ApiError apiError = (ApiError) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", apiError.toString());
                break;
        }
    }

    /**
     * 机器状况
     * http://localhost/p/test/managerTest/auth
     */
    @GetMapping("auth")
    public void auth(){
        log.info("开始测试auth");

        FrankMachineDTO bean = new FrankMachineDTO();
        bean.setId("1");
        bean.setDateTime("2");
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice("3");
        bean.setTaxVersion("4");
        bean.setEventEnum(EventEnum.STATUS);
        bean.setError(new FMError("200", "ok"));

        ApiResponse apiResponse = serviceInvokeManager.auth("FM100001", bean);
        log.info("测试结束：object = " + apiResponse.getObject());
    }


    /**
     * 机器状况
     * http://localhost/p/test/managerTest/unauth
     */
    @GetMapping("unauth")
    public void unauth(){
        log.info("开始测试unauth");

        FrankMachineDTO bean = new FrankMachineDTO();
        bean.setId("1");
        bean.setDateTime("2");
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice("3");
        bean.setTaxVersion("4");
        bean.setEventEnum(EventEnum.STATUS);
        bean.setError(new FMError("200", "ok"));

        ApiResponse  apiResponse = serviceInvokeManager.unauth("我是id", bean);
        log.info("测试结束：object = " + apiResponse.getObject());
    }


    /**
     * 机器状况
     * http://localhost/p/test/managerTest/lost
     */
    @GetMapping("lost")
    public void lost(){
        log.info("开始测试 lost");

        FrankMachineDTO bean = new FrankMachineDTO();
        bean.setId("1");
        bean.setDateTime("2");
        bean.setStatus(FMStatusEnum.ENABLED);
        bean.setPostOffice("3");
        bean.setTaxVersion("4");
        bean.setEventEnum(EventEnum.STATUS);
        bean.setError(new FMError("200", "ok"));

        ApiResponse apiResponse = serviceInvokeManager.lost("我是lost id", bean);
        log.info("测试结束：object = " + apiResponse.getObject());
    }

    /**
     * 机器状况
     * http://localhost/p/test/managerTest/publicKey
     */
    @GetMapping("publicKey")
    public void publicKey(){
        log.info("开始测试 publicKey");

        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
        publicKeyDTO.setKey("-----BEGIN PUBLIC KEY-----\n" +
                "      MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAHxZMuhGUvOwc6GKT6Y9V6+uSQmiLW\n" +
                "      9vCO4A1xy7qquqrNFmPlsQhPMZUZ62HBKDeH\n" +
                "      -----END PUBLIC KEY-----");
        publicKeyDTO.setRevision(1);
        publicKeyDTO.setExpireDate("2020418");

        ApiResponse apiResponse = serviceInvokeManager.publicKey("FM100001", publicKeyDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/rateTables
     */
    @GetMapping("rateTables")
    public void rateTables(){
        log.info("开始测试 rateTables");

        RateTableFeedbackDTO rateTableFeedbackDTO = new RateTableFeedbackDTO();
        rateTableFeedbackDTO.setTaxVersion("V 1.0");
        rateTableFeedbackDTO.setStatus(true);
        rateTableFeedbackDTO.setRcsVersions(new String[]{"aaa","bbb"});

        ApiResponse apiResponse = serviceInvokeManager.rateTables(rateTableFeedbackDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/foreseens
     */
    @GetMapping("foreseens")
    public void foreseens(){
        log.info("开始测试 foreseens");

        ForeseenProductDTO foreseenProduct = new ForeseenProductDTO();
        foreseenProduct.setProductCode("1");
        foreseenProduct.setCount(10);
        foreseenProduct.setWeight(1.0D);
        foreseenProduct.setAmount(1.0D);

        ForeseenProductDTO foreseenProduct2 = new ForeseenProductDTO();
        foreseenProduct2.setProductCode("2");
        foreseenProduct2.setCount(20);
        foreseenProduct2.setWeight(2.0D);
        foreseenProduct2.setAmount(2.0D);



        ForeseenDTO foreseenDTO = new ForeseenDTO();
        foreseenDTO.setId("id1");
        foreseenDTO.setPostOffice("123");
        foreseenDTO.setUserId("111");
        foreseenDTO.setContractId("222");
        foreseenDTO.setContractNum(10);
        foreseenDTO.setTotalCount(20);
        foreseenDTO.setProducts(new ForeseenProductDTO[]{foreseenProduct, foreseenProduct2});
        foreseenDTO.setFrankMachineId("30");
        foreseenDTO.setTaxVersion("V50");
        foreseenDTO.setMailVal(1000.0D);


        ApiResponse apiResponse = serviceInvokeManager.foreseens(foreseenDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/cancel
     */
    @GetMapping("cancel")
    public void cancel(){
        log.info("开始测试 cancel");

        String foreseenId = "id 666";
        ForeseenCancel foreseenCancel = new ForeseenCancel("不弄了，取消");

        ApiResponse apiResponse = serviceInvokeManager.cancel(null, foreseenCancel);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/transactions
     */
    @GetMapping("transactions")
    public void transactions(){
        log.info("开始测试 transactions");
        FrankDTO frank = new FrankDTO();
        frank.setDm_message("111");

        FrankDTO frank2 = new FrankDTO();
        frank2.setDm_message("222");


        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId("transactions ID ");
        transactionDTO.setForeseenId("");
        transactionDTO.setPostOffice("");
        transactionDTO.setFrankMachineId("");
        transactionDTO.setContractId("");
        transactionDTO.setContractNum(0);
        transactionDTO.setStartDateTime("");
        transactionDTO.setStopDateTime("");
        transactionDTO.setUserId("");
        transactionDTO.setCreditVal(0.0D);
        transactionDTO.setAmount(0.0D);
        transactionDTO.setCount(0);
        transactionDTO.setGraphId("");
        transactionDTO.setTaxVersion("");
        transactionDTO.setFranks(new FrankDTO[]{frank , frank2});

        ApiResponse apiResponse = serviceInvokeManager.transactions(transactionDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/refills
     */
    @GetMapping("refills")
    public void refills(){
        log.info("开始测试 refills");

        RegistersDTO registersDTO = new RegistersDTO();
        registersDTO.setId("registers ID");
        registersDTO.setContractId("");
        registersDTO.setContractNum(0);
        registersDTO.setPostofficeIndex("");
        registersDTO.setFrankMachineId("");
        registersDTO.setTimestamp("");
        registersDTO.setAscRegister(0.0D);
        registersDTO.setDecRegister(0.0D);
        registersDTO.setAmount(0.0D);
        registersDTO.setType("");


        ApiResponse apiResponse = serviceInvokeManager.refills(registersDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/stats
     */
    @GetMapping("stats")
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
        frank.setDm_message("111");

        FrankDTO frank2 = new FrankDTO();
        frank2.setDm_message("222");

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setId("statistics ID");
        statisticsDTO.setContractId("");
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


        ApiResponse apiResponse = serviceInvokeManager.stats(statisticsDTO);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }


}
