package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.PostOfficeContract;
import cc.mrbird.febs.rcs.service.IPostOfficeContractService;
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
 * 邮局-合同关系表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:46:07
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class PostOfficeContractController extends BaseController {

    private final IPostOfficeContractService postOfficeContractService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "postOfficeContract")
    public String postOfficeContractIndex(){
        return FebsUtil.view("postOfficeContract/postOfficeContract");
    }

    @GetMapping("postOfficeContract")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:list")
    public FebsResponse getAllPostOfficeContracts(PostOfficeContract postOfficeContract) {
        return new FebsResponse().success().data(postOfficeContractService.findPostOfficeContracts(postOfficeContract));
    }

    @GetMapping("postOfficeContract/list")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:list")
    public FebsResponse postOfficeContractList(QueryRequest request, PostOfficeContract postOfficeContract) {
        Map<String, Object> dataTable = getDataTable(this.postOfficeContractService.findPostOfficeContracts(request, postOfficeContract));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PostOfficeContract", exceptionMessage = "新增PostOfficeContract失败")
    @PostMapping("postOfficeContract")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:add")
    public FebsResponse addPostOfficeContract(@Valid PostOfficeContract postOfficeContract) {
        this.postOfficeContractService.createPostOfficeContract(postOfficeContract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PostOfficeContract", exceptionMessage = "删除PostOfficeContract失败")
    @GetMapping("postOfficeContract/delete")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:delete")
    public FebsResponse deletePostOfficeContract(PostOfficeContract postOfficeContract) {
        this.postOfficeContractService.deletePostOfficeContract(postOfficeContract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostOfficeContract", exceptionMessage = "修改PostOfficeContract失败")
    @PostMapping("postOfficeContract/update")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:update")
    public FebsResponse updatePostOfficeContract(PostOfficeContract postOfficeContract) {
        this.postOfficeContractService.updatePostOfficeContract(postOfficeContract);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PostOfficeContract", exceptionMessage = "导出Excel失败")
    @PostMapping("postOfficeContract/excel")
    @ResponseBody
    @RequiresPermissions("postOfficeContract:export")
    public void export(QueryRequest queryRequest, PostOfficeContract postOfficeContract, HttpServletResponse response) {
        List<PostOfficeContract> postOfficeContracts = this.postOfficeContractService.findPostOfficeContracts(queryRequest, postOfficeContract).getRecords();
        ExcelKit.$Export(PostOfficeContract.class, response).downXlsx(postOfficeContracts, false);
    }
}
