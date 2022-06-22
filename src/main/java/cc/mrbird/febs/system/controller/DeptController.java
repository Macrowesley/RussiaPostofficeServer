package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.service.IDeptService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("dept")
@ApiIgnore
public class DeptController {

    @Autowired
    IDeptService deptService;

    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "{dept.listFail}")
    @ApiOperation("get a list of department tree")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Dept.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public List<DeptTree<Dept>> getDeptTree() throws FebsException {
        return this.deptService.findDepts();
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "{dept.listFail}")
    @ApiOperation("get department tree")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Dept.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse getDeptTree(Dept dept) throws FebsException {
        List<DeptTree<Dept>> depts = this.deptService.findDepts(dept);
        return new FebsResponse().success().data(depts);
    }

    @PostMapping
    @RequiresPermissions("dept:add")
    @ControllerEndpoint(operation = "新增部门", exceptionMessage = "{dept.addFail}")
    @ApiOperation("add a department")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dept", value = "dept", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse addDept(@Valid Dept dept) {
        this.deptService.createDept(dept);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    @ControllerEndpoint(operation = "删除部门", exceptionMessage = "{dept.deleteFail}")
    @ApiOperation("delete departments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptIds", value = "deptIds", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws FebsException {
        String[] ids = deptIds.split(StringPool.COMMA);
        this.deptService.deleteDepts(ids);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("dept:update")
    @ControllerEndpoint(operation = "修改部门", exceptionMessage = "{dept.editFail}")
    @ApiOperation("Update a department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse updateDept(@Valid Dept dept) throws FebsException {
        this.deptService.updateDept(dept);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("dept:export")
    @ControllerEndpoint(exceptionMessage = "{excelFail}")
    @ApiOperation("Export excel")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws FebsException {
        List<Dept> depts = this.deptService.findDepts(dept, request);
        //ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
    }
}
