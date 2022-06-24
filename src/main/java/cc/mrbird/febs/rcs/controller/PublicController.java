package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.dto.service.ContractAddressDTO;
import cc.mrbird.febs.rcs.entity.ContractAddress;
import cc.mrbird.febs.rcs.service.IContractAddressService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller("PublicRcs")
@RequiredArgsConstructor
public class PublicController {
    @Autowired
    IContractAddressService contractAddressService;
    /**
     * 添加地址页面
     * http://localhost/p/address
     * http://localhost:88/rcs/address
     * @return
     */
    @GetMapping("/address")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    @ApiIgnore
    public String ContractAddressView() {
        log.info("访问添加邮局地址页面");
        return FebsUtil.view("rcs/contractAddress/add");
    }

    @PostMapping("/address/add")
    @ResponseBody
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    @ApiOperation("Add contract address(Only used when the machine creates an order, the address is added)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse addContractAddress(@RequestBody ContractAddressDTO contractAddressDTO){
        log.info("接收到了添加地址信息" + contractAddressDTO.toString());
        return contractAddressService.addAddressList(contractAddressDTO);
    }
}
