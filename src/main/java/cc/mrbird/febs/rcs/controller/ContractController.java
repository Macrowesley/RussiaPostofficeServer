package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.service.IContractService;

import cc.mrbird.febs.rcs.vo.ContractVO;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 合同表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@Api(description = "Add, delete, change, search for contract")
public class ContractController extends BaseController {

    @Autowired
    IContractService contractService;

    @GetMapping("contract")
    @ResponseBody
    @RequiresPermissions("contract:list")
    @ApiOperation("Get all contracts")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Contract.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @ApiIgnore
    public FebsResponse getAllContracts(Contract contract) {
        return new FebsResponse().success().data(contractService.findContracts(contract));
    }

    @GetMapping("pc/contract/list")
    @ResponseBody
    @RequiresPermissions("contract:list")
    @ApiIgnore
    public FebsResponse contractList(QueryRequest request, Contract contract) {
        Map<String, Object> dataTable = getDataTable(this.contractService.findContracts(request, contract));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping("contract/list")
    @ResponseBody
    @RequiresPermissions("contract:list")
    @ApiOperation("List for contract")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Contract.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse contractListForAnnotation(QueryRequest request, @RequestBody(required = false) Contract contract) {
        Map<String, Object> dataTable = getDataTable(this.contractService.findContracts(request, contract));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Contract", exceptionMessage = "新增Contract失败")
    @PostMapping("contract")
    @ResponseBody
    @RequiresPermissions("contract:add")
    @ApiOperation("add a contract")
    @ApiIgnore
    public FebsResponse addContract(@Valid Contract contract) {
        this.contractService.createContract(contract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Contract", exceptionMessage = "删除Contract失败")
    @GetMapping("contract/delete")
    @ResponseBody
    @RequiresPermissions("contract:delete")
    @ApiOperation("Delete a contract")
    @ApiIgnore
    public FebsResponse deleteContract(Contract contract) {
        this.contractService.deleteContract(contract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Contract", exceptionMessage = "修改Contract失败")
    @PostMapping("contract/update")
    @ResponseBody
    @RequiresPermissions("contract:update")
    @ApiOperation("Update a contract")
    @ApiIgnore
    public FebsResponse updateContract(Contract contract) {
        this.contractService.updateContract(contract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Excel", exceptionMessage = "导出Excel失败")
    @GetMapping("contract/excel")
    @ResponseBody
    @RequiresPermissions("contract:export")
    @ApiOperation("export excel")
    @ApiIgnore
    public void export(QueryRequest queryRequest, Contract contract, HttpServletResponse response) {
        List<Contract> contracts = this.contractService.findContracts(queryRequest, contract).getRecords();
        //ExcelKit.$Export(Contract.class, response).downXlsx(contracts, false);
    }

    @GetMapping("/contract/detail/{id}")
    @ResponseBody
    @ApiOperation("Get contract detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Contract.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @ApiIgnore
    public FebsResponse contractDetail(@PathVariable String id) {
        ContractVO contractVO = contractService.getVoByConractCode(id);
        return new FebsResponse().success().data(contractVO);
    }

}
