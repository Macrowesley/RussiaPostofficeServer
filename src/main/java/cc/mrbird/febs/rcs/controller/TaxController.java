package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.service.ITaxService;
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
 * 税率表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:44
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class TaxController extends BaseController {

    private final ITaxService taxService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "tax")
    public String taxIndex(){
        return FebsUtil.view("tax/tax");
    }

    @GetMapping("tax")
    @ResponseBody
    @RequiresPermissions("tax:list")
    public FebsResponse getAllTaxs(Tax tax) {
        return new FebsResponse().success().data(taxService.findTaxs(tax));
    }

    @GetMapping("tax/list")
    @ResponseBody
    @RequiresPermissions("tax:list")
    public FebsResponse taxList(QueryRequest request, Tax tax) {
        Map<String, Object> dataTable = getDataTable(this.taxService.findTaxs(request, tax));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Tax", exceptionMessage = "新增Tax失败")
    @PostMapping("tax")
    @ResponseBody
    @RequiresPermissions("tax:add")
    public FebsResponse addTax(@Valid Tax tax) {
        this.taxService.createTax(tax);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Tax", exceptionMessage = "删除Tax失败")
    @GetMapping("tax/delete")
    @ResponseBody
    @RequiresPermissions("tax:delete")
    public FebsResponse deleteTax(Tax tax) {
        this.taxService.deleteTax(tax);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Tax", exceptionMessage = "修改Tax失败")
    @PostMapping("tax/update")
    @ResponseBody
    @RequiresPermissions("tax:update")
    public FebsResponse updateTax(Tax tax) {
        this.taxService.updateTax(tax);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Tax", exceptionMessage = "导出Excel失败")
    @PostMapping("tax/excel")
    @ResponseBody
    @RequiresPermissions("tax:export")
    public void export(QueryRequest queryRequest, Tax tax, HttpServletResponse response) {
        List<Tax> taxs = this.taxService.findTaxs(queryRequest, tax).getRecords();
        ExcelKit.$Export(Tax.class, response).downXlsx(taxs, false);
    }
}
