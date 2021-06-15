package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.IPrintJobService;
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
 * 打印任务表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:44:46
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class PrintJobController extends BaseController {

    @Autowired
    IPrintJobService printJobService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "printJob")
    public String printJobIndex(){
        return FebsUtil.view("printJob/printJob");
    }

    @GetMapping("printJob")
    @ResponseBody
    @RequiresPermissions("printJob:list")
    public FebsResponse getAllPrintJobs(PrintJob printJob) {
        return new FebsResponse().success().data(printJobService.findPrintJobs(printJob));
    }

    @GetMapping("printJob/list")
    @ResponseBody
    @RequiresPermissions("printJob:list")
    public FebsResponse printJobList(QueryRequest request, PrintJob printJob) {
        Map<String, Object> dataTable = getDataTable(this.printJobService.findPrintJobs(request, printJob));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PrintJob", exceptionMessage = "新增PrintJob失败")
    @PostMapping("printJob")
    @ResponseBody
    @RequiresPermissions("printJob:add")
    public FebsResponse addPrintJob(@Valid PrintJob printJob) {
        this.printJobService.createPrintJob(printJob);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PrintJob", exceptionMessage = "删除PrintJob失败")
    @GetMapping("printJob/delete")
    @ResponseBody
    @RequiresPermissions("printJob:delete")
    public FebsResponse deletePrintJob(PrintJob printJob) {
        this.printJobService.deletePrintJob(printJob);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PrintJob", exceptionMessage = "修改PrintJob失败")
    @PostMapping("printJob/update")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    public FebsResponse updatePrintJob(PrintJob printJob) {
        this.printJobService.updatePrintJob(printJob);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PrintJob", exceptionMessage = "导出Excel失败")
    @PostMapping("printJob/excel")
    @ResponseBody
    @RequiresPermissions("printJob:export")
    public void export(QueryRequest queryRequest, PrintJob printJob, HttpServletResponse response) {
        List<PrintJob> printJobs = this.printJobService.findPrintJobs(queryRequest, printJob).getRecords();
        ExcelKit.$Export(PrintJob.class, response).downXlsx(printJobs, false);
    }
}
