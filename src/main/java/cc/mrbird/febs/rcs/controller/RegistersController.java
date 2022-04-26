package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Registers;
import cc.mrbird.febs.rcs.service.IRegistersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 【待定】 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:27
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RegistersController extends BaseController {

    @Autowired
    IRegistersService registersService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "registers")
    public String registersIndex(){
        return FebsUtil.view("registers/registers");
    }

    @GetMapping("registers")
    @ResponseBody
    @RequiresPermissions("registers:list")
    public FebsResponse getAllRegisterss(Registers registers) {
        return new FebsResponse().success().data(registersService.findRegisterss(registers));
    }

    @GetMapping("registers/list")
    @ResponseBody
    @RequiresPermissions("registers:list")
    public FebsResponse registersList(QueryRequest request, Registers registers) {
        Map<String, Object> dataTable = getDataTable(this.registersService.findRegisterss(request, registers));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Registers", exceptionMessage = "新增Registers失败")
    @PostMapping("registers")
    @ResponseBody
    @RequiresPermissions("registers:add")
    public FebsResponse addRegisters(@Valid Registers registers) {
        this.registersService.createRegisters(registers);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Registers", exceptionMessage = "删除Registers失败")
    @GetMapping("registers/delete")
    @ResponseBody
    @RequiresPermissions("registers:delete")
    public FebsResponse deleteRegisters(Registers registers) {
        this.registersService.deleteRegisters(registers);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Registers", exceptionMessage = "修改Registers失败")
    @PostMapping("registers/update")
    @ResponseBody
    @RequiresPermissions("registers:update")
    public FebsResponse updateRegisters(Registers registers) {
        this.registersService.updateRegisters(registers);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Registers", exceptionMessage = "导出Excel失败")
    @PostMapping("registers/excel")
    @ResponseBody
    @RequiresPermissions("registers:export")
    public void export(QueryRequest queryRequest, Registers registers, HttpServletResponse response) {
        List<Registers> registerss = this.registersService.findRegisterss(queryRequest, registers).getRecords();
        //ExcelKit.$Export(Registers.class, response).downXlsx(registerss, false);
    }
}
