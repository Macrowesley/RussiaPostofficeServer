package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import cc.mrbird.febs.rcs.service.IForeseenProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 预算订单产品 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:44:55
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@Api(description = "Add, delete, change, search for foreseen products")
@ApiIgnore
public class ForeseenProductController extends BaseController {

    @Autowired
    IForeseenProductService foreseenProductService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "foreseenProduct")

    public String foreseenProductIndex(){
        return FebsUtil.view("foreseenProduct/foreseenProduct");
    }

    @GetMapping("foreseenProduct")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:list")
    @ApiOperation("Get all foreseen products")
    public FebsResponse getAllForeseenProducts(ForeseenProduct foreseenProduct) {
        return new FebsResponse().success().data(foreseenProductService.findForeseenProducts(foreseenProduct));
    }

    @GetMapping("foreseenProduct/list")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:list")
    @ApiOperation("Get all foreseen products")
    public FebsResponse foreseenProductList(QueryRequest request, ForeseenProduct foreseenProduct) {
        Map<String, Object> dataTable = getDataTable(this.foreseenProductService.findForeseenProducts(request, foreseenProduct));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增ForeseenProduct", exceptionMessage = "新增ForeseenProduct失败")
    @PostMapping("foreseenProduct")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:add")
    @ApiOperation("Add a foreseen product")
    public FebsResponse addForeseenProduct(@Valid ForeseenProduct foreseenProduct) {
        this.foreseenProductService.createForeseenProduct(foreseenProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除ForeseenProduct", exceptionMessage = "删除ForeseenProduct失败")
    @GetMapping("foreseenProduct/delete")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:delete")
    @ApiOperation("Delete a foreseen product")
    public FebsResponse deleteForeseenProduct(ForeseenProduct foreseenProduct) {
        this.foreseenProductService.deleteForeseenProduct(foreseenProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改ForeseenProduct", exceptionMessage = "修改ForeseenProduct失败")
    @PostMapping("foreseenProduct/update")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:update")
    @ApiOperation("Update a foreseen product")
    public FebsResponse updateForeseenProduct(ForeseenProduct foreseenProduct) {
        this.foreseenProductService.updateForeseenProduct(foreseenProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "export excel", exceptionMessage = "导出Excel失败")
    @GetMapping("foreseenProduct/excel")
    @ResponseBody
    @RequiresPermissions("foreseenProduct:export")
    @ApiOperation("export excel")
    @ApiIgnore
    public void export(QueryRequest queryRequest, ForeseenProduct foreseenProduct, HttpServletResponse response) {
        List<ForeseenProduct> foreseenProducts = this.foreseenProductService.findForeseenProducts(queryRequest, foreseenProduct).getRecords();
        //ExcelKit.$Export(ForeseenProduct.class, response).downXlsx(foreseenProducts, false);
    }
}
