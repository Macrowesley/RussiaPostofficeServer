package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.netty.protocol.dto.AddressDTO;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.vo.UserDeviceVO;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.service.IContractAddressService;
import cc.mrbird.febs.rcs.service.IContractService;
import cc.mrbird.febs.rcs.vo.ContractVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Controller("RcsView")
@RequiredArgsConstructor
@RequestMapping(FebsConstant.VIEW_PREFIX + "rcs")
@ApiIgnore
public class ViewController extends BaseController{

    @Autowired
    IContractService contractService;



    @GetMapping("/contract")
    @RequiresPermissions("contract:view")
    public String RcsContract(HttpServletRequest request) {
        return FebsUtil.view("rcs/contract/contract");
    }

    @GetMapping("/contract/detail/{id}")
    public String contractDetail(@PathVariable String id, Model model) {
        log.info("得到的id={}",id);
        ContractVO contractVO = contractService.getVoByConractId(id);
        log.info("详情 contractVO = " + contractVO.toString());
        model.addAttribute("contract", contractVO);
        return FebsUtil.view("rcs/contract/detail");
    }

    @GetMapping("/contract/update/{id}")
    @RequiresPermissions("contract:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    public String contractUpdate(@PathVariable String id, Model model) {
        ContractVO contractVO = contractService.getVoByConractId(id);
        log.info("更新 contractVO = " + contractVO.toString());
        model.addAttribute("contract", contractVO);
        return FebsUtil.view("rcs/contract/update");
    }




}
