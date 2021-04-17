package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.FrankMachine;
import cc.mrbird.febs.rcs.service.IFrankMachineService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 机器信息 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:05
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class FrankMachineController extends BaseController {

    private final IFrankMachineService frankMachineService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "frankMachine")
    public String frankMachineIndex(){
        return FebsUtil.view("frankMachine/frankMachine");
    }

    @GetMapping("frankMachine")
    @ResponseBody
    @RequiresPermissions("frankMachine:list")
    public FebsResponse getAllFrankMachines(FrankMachine frankMachine) {
        return new FebsResponse().success().data(frankMachineService.findFrankMachines(frankMachine));
    }

    @GetMapping("frankMachine/list")
    @ResponseBody
    @RequiresPermissions("frankMachine:list")
    public FebsResponse frankMachineList(QueryRequest request, FrankMachine frankMachine) {
        Map<String, Object> dataTable = getDataTable(this.frankMachineService.findFrankMachines(request, frankMachine));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增FrankMachine", exceptionMessage = "新增FrankMachine失败")
    @PostMapping("frankMachine")
    @ResponseBody
    @RequiresPermissions("frankMachine:add")
    public FebsResponse addFrankMachine(@Valid FrankMachine frankMachine) {
        this.frankMachineService.createFrankMachine(frankMachine);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除FrankMachine", exceptionMessage = "删除FrankMachine失败")
    @GetMapping("frankMachine/delete")
    @ResponseBody
    @RequiresPermissions("frankMachine:delete")
    public FebsResponse deleteFrankMachine(FrankMachine frankMachine) {
        this.frankMachineService.deleteFrankMachine(frankMachine);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改FrankMachine", exceptionMessage = "修改FrankMachine失败")
    @PostMapping("frankMachine/update")
    @ResponseBody
    @RequiresPermissions("frankMachine:update")
    public FebsResponse updateFrankMachine(FrankMachine frankMachine) {
        this.frankMachineService.updateFrankMachine(frankMachine);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改FrankMachine", exceptionMessage = "导出Excel失败")
    @PostMapping("frankMachine/excel")
    @ResponseBody
    @RequiresPermissions("frankMachine:export")
    public void export(QueryRequest queryRequest, FrankMachine frankMachine, HttpServletResponse response) {
        List<FrankMachine> frankMachines = this.frankMachineService.findFrankMachines(queryRequest, frankMachine).getRecords();
        ExcelKit.$Export(FrankMachine.class, response).downXlsx(frankMachines, false);
    }
}
