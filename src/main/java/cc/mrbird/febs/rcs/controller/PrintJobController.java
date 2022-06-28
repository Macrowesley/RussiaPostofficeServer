package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.common.kit.EasyExcelKit;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductPcRespDTO;
import cc.mrbird.febs.rcs.dto.service.PrintJobDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobReq;
import cc.mrbird.febs.rcs.dto.ui.PrintJobResp;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.IForeseenProductService;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import cc.mrbird.febs.rcs.vo.PrintJobExcelVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
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
@Api(description = "Add, delete, change, search for print job, and operate print task")
public class PrintJobController extends BaseController {
    @Autowired
    MessageUtils messageUtils;

    @Autowired
    IPrintJobService printJobService;

    @Autowired
    RedisService redisService;



    @Autowired
    ITransactionMsgService iTransactionMsgService;

    @Autowired
    IForeseenProductService iForeseenProductService;

    @Autowired
    IPrintJobService iPrintJobService;

    @Autowired
    EasyExcelKit easyExcelKit;

    @GetMapping(FebsConstant.VIEW_PREFIX + "printJob")
    @ApiIgnore
    public String printJobIndex(){
        return FebsUtil.view("printJob/printJob");
    }

    @PostMapping("printJob/list")
    @RequiresPermissions("printJob:list")
    @ApiOperation("List for print jobs")
    @ResponseBody
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = PrintJob.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse printJobList(QueryRequest request, @RequestBody PrintJobDTO printJobDto) {
        Map<String, Object> dataTable = getDataTable(this.printJobService.findPrintJobs(request, printJobDto));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PrintJob", exceptionMessage = "新增PrintJob失败")
    @PostMapping("printJob/add")
    @ResponseBody
    @RequiresPermissions("printJob:add")
    @ApiOperation("Add a print job")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse addPrintJobApi(@Valid @RequestBody PrintJobReq printJobReq) {
        return printJobService.createPrintJobDto(printJobReq);
    }

    /**
     * 一体化项目的前端新增job
     * @param printJobReq
     * @return
     */
    @ControllerEndpoint(operation = "新增PrintJob", exceptionMessage = "新增PrintJob失败")
    @PostMapping("printJob/pc/add")
    @ResponseBody
    @RequiresPermissions("printJob:add")
    @ApiIgnore
    public FebsResponse addPcPrintJob(@Valid PrintJobReq printJobReq) {
        return printJobService.createPrintJobDto(printJobReq);
    }

    @ApiOperation("get update info")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = PrintJobResp.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @GetMapping("/printJob/update/{printJobId}")
    public FebsResponse printJobUpdate(@PathVariable int printJobId) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(printJobId);
        List<ForeseenProductPcRespDTO> foreseenProduct = iForeseenProductService.selectPcProductAdList(printJobId);

        PrintJobResp printJobResp = new PrintJobResp();

        BeanUtils.copyProperties(printJob, printJobResp);
        printJobResp.setProducts(foreseenProduct);

        return new FebsResponse().success().data(printJobResp);
    }

    @ControllerEndpoint(operation = "修改PrintJob", exceptionMessage = "修改PrintJob失败")
    @PostMapping("printJob/update/{printJobId}")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Update a print job")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse updatePrintJobApi(@PathVariable @NotBlank Integer printJobId, @RequestBody PrintJobReq printJobUpdateDto) {
        printJobUpdateDto.setId(printJobId);
        return this.printJobService.editPrintJob(printJobUpdateDto);
    }

    /**
     * 一体化项目的前端 更新
     * @param id
     * @param printJobUpdateDto
     * @return
     */
    @ApiIgnore
    @ResponseBody
    @PostMapping("printJob/pc/update")
    public FebsResponse updatePcPrintJob(PrintJobReq printJobUpdateDto) {
        log.info("更新printJob： printJobUpdateDto={}", printJobUpdateDto.toString());
        return this.printJobService.editPrintJob(printJobUpdateDto);
    }


   /* @ControllerEndpoint(operation = "删除PrintJob", exceptionMessage = "删除PrintJob失败")
    @GetMapping("printJob/delete")
    @ResponseBody
    @RequiresPermissions("printJob:delete")
    @ApiOperation("Delete a print job")
    @ApiIgnore
    public FebsResponse deletePrintJob(PrintJob printJob) {
        this.printJobService.deletePrintJob(printJob);
        return new FebsResponse().success();
    }*/



    @ControllerEndpoint(operation = "导出Excel", exceptionMessage = "导出Excel失败")
    @GetMapping("printJob/excel")
    @ResponseBody
    @RequiresPermissions("printJob:export")
    @ApiOperation("export excel")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = void.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public void export(PrintJobDTO printJobDto, HttpServletResponse response) {
        log.info("导出excel");
        try {
            List<PrintJobExcelVO> printJobExcelVOList = printJobService.selectExcelData(printJobDto);
            easyExcelKit.download(response, printJobExcelVOList,PrintJobExcelVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ControllerEndpoint(operation = "打印任务操作", exceptionMessage = "打印任务操作失败")
    @PostMapping("printJob/begin")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Operate print task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse doPrintJob(Integer id) {
        //log.info("开始打印任务操作：" + id);
        //log.info("userinfo = " + String.valueOf(FebsUtil.getCurrentUser().getUserId()));
        try {
            this.printJobService.doPrintJob(id);
        } catch (Exception e) {
//            e.printStackTrace();
            return new FebsResponse().success().data(e.getMessage());
        }
        return new FebsResponse().success().data("ok");
    }

    @ControllerEndpoint(operation = "取消打印任务操作", exceptionMessage = "取消打印任务操作失败")
    @PostMapping("printJob/cancel")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Cancel print task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @ApiIgnore
    public FebsResponse cancelPrintJob(Integer id) {
        log.info("开始取消打印任务操作：" + id);
        this.printJobService.cancelPrintJob(id);
        return new FebsResponse().success().data("ok");
    }

    @GetMapping("/printJob/detail/{printJobId}")
    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    @ApiOperation("get a print job detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = PrintJobResp.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse printDetail(@PathVariable int printJobId) {
        PrintJob printJob = iPrintJobService.getByPrintJobId(printJobId);
        List<ForeseenProductPcRespDTO> foreseenProduct = iForeseenProductService.selectPcProductAdList(printJobId);

        PrintJobResp printJobResp = new PrintJobResp();

        BeanUtils.copyProperties(printJob, printJobResp);
        printJobResp.setProducts(foreseenProduct);

        return new FebsResponse().success().data(printJobResp);
    }

}
