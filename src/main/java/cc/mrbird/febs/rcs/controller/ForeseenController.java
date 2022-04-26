package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.service.IForeseenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 预算订单 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:44:51
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class ForeseenController extends BaseController {

    @Autowired
    IForeseenService foreseenService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "foreseen")
    public String foreseenIndex(){
        return FebsUtil.view("foreseen/foreseen");
    }

    @GetMapping("foreseen")
    @ResponseBody
    @RequiresPermissions("foreseen:list")
    public FebsResponse getAllForeseens(Foreseen foreseen) {
        return new FebsResponse().success().data(foreseenService.findForeseens(foreseen));
    }

    @GetMapping("foreseen/list")
    @ResponseBody
    @RequiresPermissions("foreseen:list")
    public FebsResponse foreseenList(QueryRequest request, Foreseen foreseen) {
        Map<String, Object> dataTable = getDataTable(this.foreseenService.findForeseens(request, foreseen));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping(FebsConstant.VIEW_PREFIX +"foreseen/detail/{foreseenId}")
    @ResponseBody
    public FebsResponse foreseenInfo(@PathVariable String foreseenId){
        return new FebsResponse().success().data(foreseenService.getForeseenDetail(foreseenId));
    }

    @ControllerEndpoint(operation = "新增Foreseen", exceptionMessage = "新增Foreseen失败")
    @PostMapping("foreseen")
    @ResponseBody
    @RequiresPermissions("foreseen:add")
    public FebsResponse addForeseen(@Valid Foreseen foreseen) {
        this.foreseenService.createForeseen(foreseen);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Foreseen", exceptionMessage = "删除Foreseen失败")
    @GetMapping("foreseen/delete")
    @ResponseBody
    @RequiresPermissions("foreseen:delete")
    public FebsResponse deleteForeseen(Foreseen foreseen) {
        this.foreseenService.deleteForeseen(foreseen);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Foreseen", exceptionMessage = "修改Foreseen失败")
    @PostMapping("foreseen/update")
    @ResponseBody
    @RequiresPermissions("foreseen:update")
    public FebsResponse updateForeseen(Foreseen foreseen) {
        this.foreseenService.updateForeseen(foreseen);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Foreseen", exceptionMessage = "导出Excel失败")
    @PostMapping("foreseen/excel")
    @ResponseBody
    @RequiresPermissions("foreseen:export")
    public void export(QueryRequest queryRequest, Foreseen foreseen, HttpServletResponse response) {
        List<Foreseen> foreseens = this.foreseenService.findForeseens(queryRequest, foreseen).getRecords();
        //ExcelKit.$Export(Foreseen.class, response).downXlsx(foreseens, false);
    }
}
