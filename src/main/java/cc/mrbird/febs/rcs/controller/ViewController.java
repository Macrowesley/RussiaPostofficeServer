package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductPcRespDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobReq;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Transaction;
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

    @GetMapping("/printJob/detail/{printJobId}")
    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printDetail(@PathVariable int printJobId, Model model) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(printJobId);
        List<ForeseenProductPcRespDTO> productAdList = iForeseenProductService.selectPcProductAdList(printJobId);

        model.addAttribute("printJob",printJob);
        model.addAttribute("foreseenProduct",productAdList);

        return FebsUtil.view("rcs/printJob/printDetail");
    }

    @GetMapping("/printJob/update/{printJobId}")
    @RequiresPermissions("printJob:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String printJobUpdate(@PathVariable int printJobId, Model model) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(printJobId);
        List<ForeseenProductPcRespDTO> foreseenProduct = iForeseenProductService.selectPcProductAdList(printJobId);
        model.addAttribute("printJob",printJob);
        model.addAttribute("foreseenProduct",foreseenProduct);
        return FebsUtil.view("rcs/printJob/printJobUpdate");
    }

    @GetMapping("/printJob/msgDetail/{transactionId}")
    @RequiresPermissions("transactionMsg:list")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String msgDetail(@PathVariable String transactionId, Model model) {
//        model.addAttribute("transactionId",transactionId);
        return FebsUtil.view("rcs/printJob/msgDetail");
    }

    @GetMapping("/audit")
//    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String audit(Model model) {
        return FebsUtil.view("audit/audit");
    }

    @GetMapping("/job")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String job(Model model) {
        return FebsUtil.view("job/job");
    }

    @GetMapping("/log")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String log(Model model) {
        return FebsUtil.view("job/jobLog");
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
