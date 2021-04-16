package cc.mrbird.febs.test.controller;


import cc.mrbird.febs.asu.ServiceInvokeManager;
import cc.mrbird.febs.asu.enums.EventEnum;
import cc.mrbird.febs.asu.enums.FMStatusEnum;
import cc.mrbird.febs.asu.enums.ResultEnum;
import cc.mrbird.febs.asu.dto.manager.*;
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
        FrankMachine bean = new FrankMachine();
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
                FrankMachine frankMachine = (FrankMachine) apiResponse.getObject();
                log.info("从manager服务器得到的数据 {}", frankMachine.toString());
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

        FrankMachine bean = new FrankMachine();
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

        FrankMachine bean = new FrankMachine();
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

        FrankMachine bean = new FrankMachine();
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

        PublicKey publicKey = new PublicKey();
        publicKey.setKey("-----BEGIN PUBLIC KEY-----\n" +
                "      MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAHxZMuhGUvOwc6GKT6Y9V6+uSQmiLW\n" +
                "      9vCO4A1xy7qquqrNFmPlsQhPMZUZ62HBKDeH\n" +
                "      -----END PUBLIC KEY-----");
        publicKey.setRevision(1);
        publicKey.setExpireDate("2020418");

        ApiResponse apiResponse = serviceInvokeManager.publicKey("FM100001", publicKey);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/rateTables
     */
    @GetMapping("rateTables")
    public void rateTables(){
        log.info("开始测试 rateTables");

        RateTableFeedback rateTableFeedback = new RateTableFeedback();
        rateTableFeedback.setTaxVersion("V 1.0");
        rateTableFeedback.setStatus(true);
        rateTableFeedback.setRcsVersions(new String[]{"aaa","bbb"});

        ApiResponse apiResponse = serviceInvokeManager.rateTables(rateTableFeedback);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/foreseens
     */
    @GetMapping("foreseens")
    public void foreseens(){
        log.info("开始测试 foreseens");

        ForeseenProduct foreseenProduct = new ForeseenProduct();
        foreseenProduct.setProductCode("1");
        foreseenProduct.setCount(10);
        foreseenProduct.setWeight(1.0D);
        foreseenProduct.setAmount(1.0D);

        ForeseenProduct foreseenProduct2 = new ForeseenProduct();
        foreseenProduct2.setProductCode("2");
        foreseenProduct2.setCount(20);
        foreseenProduct2.setWeight(2.0D);
        foreseenProduct2.setAmount(2.0D);



        Foreseen foreseen = new Foreseen();
        foreseen.setId("id1");
        foreseen.setPostOffice("123");
        foreseen.setUserId("111");
        foreseen.setContractId("222");
        foreseen.setContractNum(10);
        foreseen.setTotalCount(20);
        foreseen.setProducts(new ForeseenProduct[]{foreseenProduct,foreseenProduct2});
        foreseen.setFrankMachineId("30");
        foreseen.setTaxVersion("V50");
        foreseen.setMailVal(1000.0D);


        ApiResponse apiResponse = serviceInvokeManager.foreseens(foreseen);
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
        Frank frank = new Frank();
        frank.setDm_message("111");

        Frank frank2 = new Frank();
        frank2.setDm_message("222");


        Transaction transaction = new Transaction();
        transaction.setId("transactions ID ");
        transaction.setForeseenId("");
        transaction.setPostOffice("");
        transaction.setFrankMachineId("");
        transaction.setContractId("");
        transaction.setContractNum(0);
        transaction.setStartDateTime("");
        transaction.setStopDateTime("");
        transaction.setUserId("");
        transaction.setCreditVal(0.0D);
        transaction.setAmount(0.0D);
        transaction.setCount(0);
        transaction.setGraphId("");
        transaction.setTaxVersion("");
        transaction.setFranks(new Frank[]{frank , frank2});

        ApiResponse apiResponse = serviceInvokeManager.transactions(transaction);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/refills
     */
    @GetMapping("refills")
    public void refills(){
        log.info("开始测试 refills");

        Registers registers = new Registers();
        registers.setId("registers ID");
        registers.setContractId("");
        registers.setContractNum(0);
        registers.setPostofficeIndex("");
        registers.setFrankMachineId("");
        registers.setTimestamp("");
        registers.setAscRegister(0.0D);
        registers.setDecRegister(0.0D);
        registers.setAmount(0.0D);
        registers.setType("");


        ApiResponse apiResponse = serviceInvokeManager.refills(registers);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }

    /**
     * http://localhost/p/test/managerTest/stats
     */
    @GetMapping("stats")
    public void stats(){
        log.info("开始测试 stats");
        TransactionData transactionData1 = new TransactionData();
        transactionData1.setDateTime("");
        transactionData1.setProductCode("");
        transactionData1.setWeight(0);
        transactionData1.setCount(0);
        transactionData1.setAmount(0.0D);
        transactionData1.setTaxVersion("");

        TransactionData transactionData2 = new TransactionData();
        transactionData2.setDateTime("");
        transactionData2.setProductCode("");
        transactionData2.setWeight(0);
        transactionData2.setCount(0);
        transactionData2.setAmount(0.0D);
        transactionData2.setTaxVersion("");

        Frank frank = new Frank();
        frank.setDm_message("111");

        Frank frank2 = new Frank();
        frank2.setDm_message("222");

        Statistics statistics = new Statistics();
        statistics.setId("statistics ID");
        statistics.setContractId("");
        statistics.setContractNum(0);
        statistics.setPostOffice("");
        statistics.setFrankMachineId("");
        statistics.setFrankingPeriodStart("");
        statistics.setFrankingPeriodEnd("");
        statistics.setTotalAmount(0.0D);
        statistics.setInitialPieceCounter(0L);
        statistics.setFinalPieceCounter(0L);
        statistics.setTransactions(new TransactionData[]{transactionData1, transactionData2});
        statistics.setFranks(new Frank[]{frank,frank2});


        ApiResponse apiResponse = serviceInvokeManager.stats(statistics);
        log.info("测试结束：object = " + apiResponse.getObject().toString());
    }


}
