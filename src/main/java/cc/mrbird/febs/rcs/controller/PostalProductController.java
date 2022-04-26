package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.PostalProduct;
import cc.mrbird.febs.rcs.service.IPostalProductService;

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
 * 邮政产品表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:46:18
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class PostalProductController extends BaseController {

    @Autowired
    IPostalProductService postalProductService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "postalProduct")
    public String postalProductIndex(){
        return FebsUtil.view("postalProduct/postalProduct");
    }

    @GetMapping("postalProduct")
    @ResponseBody
    @RequiresPermissions("postalProduct:list")
    public FebsResponse getAllPostalProducts(PostalProduct postalProduct) {
        return new FebsResponse().success().data(postalProductService.findPostalProducts(postalProduct));
    }

    @GetMapping("postalProduct/list")
    @ResponseBody
    @RequiresPermissions("postalProduct:list")
    public FebsResponse postalProductList(QueryRequest request, PostalProduct postalProduct) {
        Map<String, Object> dataTable = getDataTable(this.postalProductService.findPostalProducts(request, postalProduct));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PostalProduct", exceptionMessage = "新增PostalProduct失败")
    @PostMapping("postalProduct")
    @ResponseBody
    @RequiresPermissions("postalProduct:add")
    public FebsResponse addPostalProduct(@Valid PostalProduct postalProduct) {
        this.postalProductService.createPostalProduct(postalProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PostalProduct", exceptionMessage = "删除PostalProduct失败")
    @GetMapping("postalProduct/delete")
    @ResponseBody
    @RequiresPermissions("postalProduct:delete")
    public FebsResponse deletePostalProduct(PostalProduct postalProduct) {
        this.postalProductService.deletePostalProduct(postalProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostalProduct", exceptionMessage = "修改PostalProduct失败")
    @PostMapping("postalProduct/update")
    @ResponseBody
    @RequiresPermissions("postalProduct:update")
    public FebsResponse updatePostalProduct(PostalProduct postalProduct) {
        this.postalProductService.updatePostalProduct(postalProduct);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostalProduct", exceptionMessage = "导出Excel失败")
    @PostMapping("postalProduct/excel")
    @ResponseBody
    @RequiresPermissions("postalProduct:export")
    public void export(QueryRequest queryRequest, PostalProduct postalProduct, HttpServletResponse response) {
        List<PostalProduct> postalProducts = this.postalProductService.findPostalProducts(queryRequest, postalProduct).getRecords();
        //ExcelKit.$Export(PostalProduct.class, response).downXlsx(postalProducts, false);
    }
}
