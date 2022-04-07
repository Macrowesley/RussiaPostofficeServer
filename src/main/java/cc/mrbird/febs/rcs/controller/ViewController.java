package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.license.LicenseVerifyUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.dto.ui.PrintJobAddDto;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.service.*;
import cc.mrbird.febs.rcs.vo.ContractVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller("RcsView")
@RequiredArgsConstructor
@RequestMapping(FebsConstant.VIEW_PREFIX + "rcs")
@ApiIgnore
public class ViewController extends BaseController{

    @Autowired
    IContractService contractService;

    @Autowired
    IPrintJobService iPrintJobService;

    @Autowired
    IForeseenProductService iForeseenProductService;

    @Autowired
    IForeseenService foreseenService;

    @Autowired
    ITransactionService transactionService;

    @Autowired
    ITransactionMsgService iTransactionMsgService;

    @GetMapping("/contract")
    @RequiresPermissions("contract:view")
    public String RcsContract(HttpServletRequest request) {
        return FebsUtil.view("rcs/contract/contract");
    }

    @GetMapping("/contract/detail/{id}")
    public String contractDetail(@PathVariable String id, Model model) {
        log.info("得到的id={}",id);
        ContractVO contractVO = contractService.getVoByConractCode(id);
        log.info("详情 contractVO = " + contractVO.toString());
        model.addAttribute("contract", contractVO);
        return FebsUtil.view("rcs/contract/detail");
    }

    @GetMapping("/contract/update/{id}")
    @RequiresPermissions("contract:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String contractUpdate(@PathVariable String id, Model model) {
        ContractVO contractVO = contractService.getVoByConractCode(id);
        log.info("更新 contractVO = " + contractVO.toString());
        model.addAttribute("contract", contractVO);
        return FebsUtil.view("rcs/contract/update");
    }

    @GetMapping("/printJob")
//    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printJob(Model model) {
        return FebsUtil.view("rcs/printJob/printJob");
    }


    @GetMapping("/printJob/add")
//    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printJobAdd(Model model) {
        return FebsUtil.view("rcs/printJob/printJobAdd");
    }

    @GetMapping("/printJob/detail/{id}")
    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printDetail(@PathVariable int id, Model model) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(id);
        ArrayList<ForeseenProduct> foreseenProduct = iForeseenProductService.getByPrintJobId(id);
        PrintJobAddDto printJobAddDto = null;
        model.addAttribute("printJob",printJob);
        model.addAttribute("foreseenProduct",foreseenProduct);
        //printJobAddDto.setProducts(foreseenProduct);
        //System.out.println("printJobAddDto:"+JSON.toJSONString(printJobAddDto));
        return FebsUtil.view("rcs/printJob/printDetail");
    }

    @GetMapping("/printJob/update/{id}")
    @RequiresPermissions("printJob:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printJobUpdate(@PathVariable int id, Model model) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(id);
        //System.out.println(JSON.toJSONString(printJob));
        ArrayList<ForeseenProduct> foreseenProduct = iForeseenProductService.getByPrintJobId(id);
        //System.out.println(JSON.toJSONString(foreseenProduct));
        PrintJobAddDto printJobAddDto = null;
//        PrintJob printJob1=null;
//        BeanUtils.copyProperties(printJob,printJob1);
        model.addAttribute("printJob",printJob);
        model.addAttribute("foreseenProduct",foreseenProduct);
        //printJobAddDto.setProducts(foreseenProduct);
        //System.out.println("printJobAddDto:"+JSON.toJSONString(printJobAddDto));
        return FebsUtil.view("rcs/printJob/printJobUpdate");
    }

    @GetMapping("/printJob/msgDetail/{transactionId}")
    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String msgDetail(@PathVariable String transactionId, Model model) {
//        model.addAttribute("transactionId",transactionId);
        return FebsUtil.view("rcs/printJob/msgDetail");
    }

    @GetMapping("/foreseen/detail/{foreseenId}")
    public String foreseenInfo(@PathVariable String foreseenId, Model model) {
        Foreseen foreseen = foreseenService.getForeseenDetail(foreseenId);
        log.info("详情 foreseen = " + foreseen.toString());
        model.addAttribute("foreseen", foreseen);
        return FebsUtil.view("rcs/foreseen/foreseenDetail");
    }

    @GetMapping("/transaction/detail/{transactionId}")
    public String transactionInfo(@PathVariable String transactionId, Model model) {
        log.info("得到的transactionId={}",transactionId);
        Transaction transaction= transactionService.getTransactionDetail(transactionId);
        log.info("详情 foreseen = " + transaction.toString());
        model.addAttribute("transaction", transaction);
        return FebsUtil.view("rcs/transaction/transactionDetail");
    }
}
