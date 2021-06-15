package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.PostOffice;
import cc.mrbird.febs.rcs.service.IPostOfficeService;
import com.wuwenze.poi.ExcelKit;
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
 * 合同表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:52
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class PostOfficeController extends BaseController {

    @Autowired
    IPostOfficeService postOfficeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "postOffice")
    public String postOfficeIndex(){
        return FebsUtil.view("postOffice/postOffice");
    }

    @GetMapping("postOffice")
    @ResponseBody
    @RequiresPermissions("postOffice:list")
    public FebsResponse getAllPostOffices(PostOffice postOffice) {
        return new FebsResponse().success().data(postOfficeService.findPostOffices(postOffice));
    }

    @GetMapping("postOffice/list")
    @ResponseBody
    @RequiresPermissions("postOffice:list")
    public FebsResponse postOfficeList(QueryRequest request, PostOffice postOffice) {
        Map<String, Object> dataTable = getDataTable(this.postOfficeService.findPostOffices(request, postOffice));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PostOffice", exceptionMessage = "新增PostOffice失败")
    @PostMapping("postOffice")
    @ResponseBody
    @RequiresPermissions("postOffice:add")
    public FebsResponse addPostOffice(@Valid PostOffice postOffice) {
        this.postOfficeService.createPostOffice(postOffice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PostOffice", exceptionMessage = "删除PostOffice失败")
    @GetMapping("postOffice/delete")
    @ResponseBody
    @RequiresPermissions("postOffice:delete")
    public FebsResponse deletePostOffice(PostOffice postOffice) {
        this.postOfficeService.deletePostOffice(postOffice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostOffice", exceptionMessage = "修改PostOffice失败")
    @PostMapping("postOffice/update")
    @ResponseBody
    @RequiresPermissions("postOffice:update")
    public FebsResponse updatePostOffice(PostOffice postOffice) {
        this.postOfficeService.updatePostOffice(postOffice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostOffice", exceptionMessage = "导出Excel失败")
    @PostMapping("postOffice/excel")
    @ResponseBody
    @RequiresPermissions("postOffice:export")
    public void export(QueryRequest queryRequest, PostOffice postOffice, HttpServletResponse response) {
        List<PostOffice> postOffices = this.postOfficeService.findPostOffices(queryRequest, postOffice).getRecords();
        ExcelKit.$Export(PostOffice.class, response).downXlsx(postOffices, false);
    }
}
