package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.TaxRate;
import cc.mrbird.febs.rcs.service.ITaxRateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 税率细节表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:55
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@ApiIgnore
public class TaxRateController extends BaseController {

    @Autowired
    ITaxRateService taxRateService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "taxRate")
    public String taxRateIndex(){
        return FebsUtil.view("taxRate/taxRate");
    }

    @GetMapping("taxRate")
    @ResponseBody
    @RequiresPermissions("taxRate:list")
    public FebsResponse getAllTaxRates(TaxRate taxRate) {
        return new FebsResponse().success().data(taxRateService.findTaxRates(taxRate));
    }

    @GetMapping("taxRate/list")
    @ResponseBody
    @RequiresPermissions("taxRate:list")
    public FebsResponse taxRateList(QueryRequest request, TaxRate taxRate) {
        Map<String, Object> dataTable = getDataTable(this.taxRateService.findTaxRates(request, taxRate));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增TaxRate", exceptionMessage = "新增TaxRate失败")
    @PostMapping("taxRate")
    @ResponseBody
    @RequiresPermissions("taxRate:add")
    public FebsResponse addTaxRate(@Valid TaxRate taxRate) {
        this.taxRateService.createTaxRate(taxRate);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除TaxRate", exceptionMessage = "删除TaxRate失败")
    @GetMapping("taxRate/delete")
    @ResponseBody
    @RequiresPermissions("taxRate:delete")
    public FebsResponse deleteTaxRate(TaxRate taxRate) {
        this.taxRateService.deleteTaxRate(taxRate);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TaxRate", exceptionMessage = "修改TaxRate失败")
    @PostMapping("taxRate/update")
    @ResponseBody
    @RequiresPermissions("taxRate:update")
    public FebsResponse updateTaxRate(TaxRate taxRate) {
        this.taxRateService.updateTaxRate(taxRate);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TaxRate", exceptionMessage = "导出Excel失败")
    @PostMapping("taxRate/excel")
    @ResponseBody
    @RequiresPermissions("taxRate:export")
    public void export(QueryRequest queryRequest, TaxRate taxRate, HttpServletResponse response) {
        List<TaxRate> taxRates = this.taxRateService.findTaxRates(queryRequest, taxRate).getRecords();
        //ExcelKit.$Export(TaxRate.class, response).downXlsx(taxRates, false);
    }
}
