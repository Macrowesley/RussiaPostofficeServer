package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.TransactionData;
import cc.mrbird.febs.rcs.service.ITransactionDataService;

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
 * 交易数据表【待定】 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:46:25
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@ApiIgnore
public class TransactionDataController extends BaseController {

    @Autowired
    ITransactionDataService transactionDataService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "transactionData")
    public String transactionDataIndex(){
        return FebsUtil.view("transactionData/transactionData");
    }

    @GetMapping("transactionData")
    @ResponseBody
    @RequiresPermissions("transactionData:list")
    public FebsResponse getAllTransactionDatas(TransactionData transactionData) {
        return new FebsResponse().success().data(transactionDataService.findTransactionDatas(transactionData));
    }

    @GetMapping("transactionData/list")
    @ResponseBody
    @RequiresPermissions("transactionData:list")
    public FebsResponse transactionDataList(QueryRequest request, TransactionData transactionData) {
        Map<String, Object> dataTable = getDataTable(this.transactionDataService.findTransactionDatas(request, transactionData));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增TransactionData", exceptionMessage = "新增TransactionData失败")
    @PostMapping("transactionData")
    @ResponseBody
    @RequiresPermissions("transactionData:add")
    public FebsResponse addTransactionData(@Valid TransactionData transactionData) {
        this.transactionDataService.createTransactionData(transactionData);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除TransactionData", exceptionMessage = "删除TransactionData失败")
    @GetMapping("transactionData/delete")
    @ResponseBody
    @RequiresPermissions("transactionData:delete")
    public FebsResponse deleteTransactionData(TransactionData transactionData) {
        this.transactionDataService.deleteTransactionData(transactionData);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TransactionData", exceptionMessage = "修改TransactionData失败")
    @PostMapping("transactionData/update")
    @ResponseBody
    @RequiresPermissions("transactionData:update")
    public FebsResponse updateTransactionData(TransactionData transactionData) {
        this.transactionDataService.updateTransactionData(transactionData);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TransactionData", exceptionMessage = "导出Excel失败")
    @PostMapping("transactionData/excel")
    @ResponseBody
    @RequiresPermissions("transactionData:export")
    public void export(QueryRequest queryRequest, TransactionData transactionData, HttpServletResponse response) {
        List<TransactionData> transactionDatas = this.transactionDataService.findTransactionDatas(queryRequest, transactionData).getRecords();
        //ExcelKit.$Export(TransactionData.class, response).downXlsx(transactionDatas, false);
    }
}
