package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.FmStatusLog;
import cc.mrbird.febs.rcs.service.IFmStatusLogService;

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
 * 机器状态变更表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:44:22
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@ApiIgnore
public class FmStatusLogController extends BaseController {

    @Autowired
    IFmStatusLogService fmStatusLogService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "fmStatusLog")
    public String fmStatusLogIndex(){
        return FebsUtil.view("fmStatusLog/fmStatusLog");
    }

    @GetMapping("fmStatusLog")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:list")
    public FebsResponse getAllFmStatusLogs(FmStatusLog fmStatusLog) {
        return new FebsResponse().success().data(fmStatusLogService.findFmStatusLogs(fmStatusLog));
    }

    @GetMapping("fmStatusLog/list")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:list")
    public FebsResponse fmStatusLogList(QueryRequest request, FmStatusLog fmStatusLog) {
        Map<String, Object> dataTable = getDataTable(this.fmStatusLogService.findFmStatusLogs(request, fmStatusLog));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增FmStatusLog", exceptionMessage = "新增FmStatusLog失败")
    @PostMapping("fmStatusLog")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:add")
    public FebsResponse addFmStatusLog(@Valid FmStatusLog fmStatusLog) {
        this.fmStatusLogService.createFmStatusLog(fmStatusLog);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除FmStatusLog", exceptionMessage = "删除FmStatusLog失败")
    @GetMapping("fmStatusLog/delete")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:delete")
    public FebsResponse deleteFmStatusLog(FmStatusLog fmStatusLog) {
        this.fmStatusLogService.deleteFmStatusLog(fmStatusLog);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改FmStatusLog", exceptionMessage = "修改FmStatusLog失败")
    @PostMapping("fmStatusLog/update")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:update")
    public FebsResponse updateFmStatusLog(FmStatusLog fmStatusLog) {
        this.fmStatusLogService.updateFmStatusLog(fmStatusLog);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改FmStatusLog", exceptionMessage = "导出Excel失败")
    @PostMapping("fmStatusLog/excel")
    @ResponseBody
    @RequiresPermissions("fmStatusLog:export")
    public void export(QueryRequest queryRequest, FmStatusLog fmStatusLog, HttpServletResponse response) {
        List<FmStatusLog> fmStatusLogs = this.fmStatusLogService.findFmStatusLogs(queryRequest, fmStatusLog).getRecords();
        //ExcelKit.$Export(FmStatusLog.class, response).downXlsx(fmStatusLogs, false);
    }
}
