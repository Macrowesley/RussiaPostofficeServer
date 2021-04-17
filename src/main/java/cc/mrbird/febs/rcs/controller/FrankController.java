package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Frank;
import cc.mrbird.febs.rcs.service.IFrankService;
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
 * 【待定】这个表的父表是哪个？Statistics还是Transaction Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:32
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class FrankController extends BaseController {

    private final IFrankService frankService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "frank")
    public String frankIndex(){
        return FebsUtil.view("frank/frank");
    }

    @GetMapping("frank")
    @ResponseBody
    @RequiresPermissions("frank:list")
    public FebsResponse getAllFranks(Frank frank) {
        return new FebsResponse().success().data(frankService.findFranks(frank));
    }

    @GetMapping("frank/list")
    @ResponseBody
    @RequiresPermissions("frank:list")
    public FebsResponse frankList(QueryRequest request, Frank frank) {
        Map<String, Object> dataTable = getDataTable(this.frankService.findFranks(request, frank));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Frank", exceptionMessage = "新增Frank失败")
    @PostMapping("frank")
    @ResponseBody
    @RequiresPermissions("frank:add")
    public FebsResponse addFrank(@Valid Frank frank) {
        this.frankService.createFrank(frank);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Frank", exceptionMessage = "删除Frank失败")
    @GetMapping("frank/delete")
    @ResponseBody
    @RequiresPermissions("frank:delete")
    public FebsResponse deleteFrank(Frank frank) {
        this.frankService.deleteFrank(frank);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Frank", exceptionMessage = "修改Frank失败")
    @PostMapping("frank/update")
    @ResponseBody
    @RequiresPermissions("frank:update")
    public FebsResponse updateFrank(Frank frank) {
        this.frankService.updateFrank(frank);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Frank", exceptionMessage = "导出Excel失败")
    @PostMapping("frank/excel")
    @ResponseBody
    @RequiresPermissions("frank:export")
    public void export(QueryRequest queryRequest, Frank frank, HttpServletResponse response) {
        List<Frank> franks = this.frankService.findFranks(queryRequest, frank).getRecords();
        ExcelKit.$Export(Frank.class, response).downXlsx(franks, false);
    }
}
