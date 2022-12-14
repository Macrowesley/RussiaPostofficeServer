package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.license.LicenseVerifyUtils;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.common.kit.EasyExcelKit;
import cc.mrbird.febs.rcs.dto.service.PrintJobDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobReq;
import cc.mrbird.febs.rcs.dto.ui.PrintJobUpdateDto;
import cc.mrbird.febs.rcs.entity.ForeseenProduct;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.IForeseenProductService;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;

import cc.mrbird.febs.rcs.vo.PrintJobExcelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ??????????????? Controller
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
    LicenseVerifyUtils verifyUtils;

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

    @GetMapping("pc/printJob/list")
    @RequiresPermissions("printJob:list")
    @ResponseBody
    @ApiIgnore
    public FebsResponse printJobList(QueryRequest request, PrintJobDTO printJobDto) {
        Map<String, Object> dataTable = getDataTable(this.printJobService.findPrintJobs(request, printJobDto));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping("printJob/list")
    @RequiresPermissions("printJob:list")
    @ApiOperation("List for print jobs")
    @ResponseBody
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = PrintJob.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "????????????")
    })
    public FebsResponse printJobListForAnnotation(QueryRequest request, @RequestBody PrintJobDTO printJobDto) {
        Map<String, Object> dataTable = getDataTable(this.printJobService.findPrintJobs(request, printJobDto));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "??????PrintJob", exceptionMessage = "??????PrintJob??????")
    @PostMapping("printJob/add")
    @ResponseBody
    @RequiresPermissions("printJob:add")
    @ApiOperation("Add a print job")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    public FebsResponse addPrintJobApi(@Valid @RequestBody PrintJobReq printJobReq) {
        return printJobService.createPrintJobDto(printJobReq);
    }

    /**
     * ??????????????????????????????job
     * @param printJobReq
     * @return
     */
    @ControllerEndpoint(operation = "??????PrintJob", exceptionMessage = "??????PrintJob??????")
    @PostMapping("printJob/pc/add")
    @ResponseBody
    @RequiresPermissions("printJob:add")
    @ApiIgnore
    public FebsResponse addPcPrintJob(@Valid PrintJobReq printJobReq) {
        return printJobService.createPrintJobDto(printJobReq);
    }

    @ControllerEndpoint(operation = "??????PrintJob", exceptionMessage = "??????PrintJob??????")
    @PostMapping("printJob/update/{id}")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Update a print job")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    public FebsResponse updatePrintJobApi(@PathVariable int id, @RequestBody PrintJobReq printJobUpdateDto) {
        return this.printJobService.editPrintJob(printJobUpdateDto);
    }

    /**
     * ???????????????????????? ??????
     * @param id
     * @param printJobUpdateDto
     * @return
     */
    @ApiIgnore
    @ResponseBody
    @PostMapping("printJob/pc/update/{id}")
    public FebsResponse updatePcPrintJob(@PathVariable int id, PrintJobReq printJobUpdateDto) {
        return this.printJobService.editPrintJob(printJobUpdateDto);
    }


    @ControllerEndpoint(operation = "??????PrintJob", exceptionMessage = "??????PrintJob??????")
    @GetMapping("printJob/delete")
    @ResponseBody
    @RequiresPermissions("printJob:delete")
    @ApiOperation("Delete a print job")
    @ApiIgnore
    public FebsResponse deletePrintJob(PrintJob printJob) {
        this.printJobService.deletePrintJob(printJob);
        return new FebsResponse().success();
    }



    @ControllerEndpoint(operation = "??????Excel", exceptionMessage = "??????Excel??????")
    @GetMapping("printJob/excel")
    @ResponseBody
    @RequiresPermissions("printJob:export")
    @ApiOperation("export excel")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = void.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    public void export(PrintJobDTO printJobDto, HttpServletResponse response) {
        log.info("??????excel");
        try {
            List<PrintJobExcelVO> printJobExcelVOList = printJobService.selectExcelData(printJobDto);
            easyExcelKit.download(response, printJobExcelVOList,PrintJobExcelVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ControllerEndpoint(operation = "??????????????????", exceptionMessage = "????????????????????????")
    @PostMapping("printJob/begin")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Operate print task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    public FebsResponse doPrintJob(Integer id) {
        //log.info("???????????????????????????" + id);
        //log.info("userinfo = " + String.valueOf(FebsUtil.getCurrentUser().getUserId()));
        try {
            this.printJobService.doPrintJob(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new FebsResponse().success().data(e.getMessage());
        }
        return new FebsResponse().success().data("ok");
    }

    @ControllerEndpoint(operation = "????????????????????????", exceptionMessage = "??????????????????????????????")
    @PostMapping("printJob/cancel")
    @ResponseBody
    @RequiresPermissions("printJob:update")
    @ApiOperation("Cancel print task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    @ApiIgnore
    public FebsResponse cancelPrintJob(Integer id) {
        log.info("?????????????????????????????????" + id);
        this.printJobService.cancelPrintJob(id);
        return new FebsResponse().success().data("ok");
    }

    @GetMapping("/printJob/detail/{id}")
    @RequiresPermissions("printJob:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_contract_view", isApi = false)
    @ApiOperation("get a print job detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = PrintJob.class),
            @ApiResponse(code = 500, message = "????????????")
    })
    public FebsResponse printDetail(@PathVariable int id) {
        Map data = new HashMap();
        PrintJob printJob = iPrintJobService.getByPrintJobId(id);
        ArrayList<ForeseenProduct> foreseenProduct = iForeseenProductService.getByPrintJobId(id);
        PrintJobReq printJobReq = null;
        data.put("printJob",printJob);
        data.put("foreseenProduct",foreseenProduct);
        //printJobAddDto.setProducts(foreseenProduct);
        //System.out.println("printJobAddDto:"+JSON.toJSONString(printJobAddDto));
        return new FebsResponse().success().data(data);
    }

//    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/update/{username}")
//    @RequiresPermissions("user:update")
//    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_view", isApi = false)
//    public String systemUserUpdate(@PathVariable String username, Model model) {
//        //resolveUserModel(username, model, false);
//        model.addAttribute("roleId", FebsUtil.getCurrentUser().getRoleId());
//        return FebsUtil.view("system/user/userUpdate");
//    }
}
