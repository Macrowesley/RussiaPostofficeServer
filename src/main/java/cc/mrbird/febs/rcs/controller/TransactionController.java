package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.service.ITransactionService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 交易表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@Api(description = "Add, delete, change, search for transaction")
@ApiIgnore
public class TransactionController extends BaseController {

    @Autowired
    ITransactionService transactionService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "transaction")

    public String transactionIndex(){
        return FebsUtil.view("transaction/transaction");
    }

    @GetMapping("transaction")
    @ResponseBody
    @RequiresPermissions("transaction:list")
    @ApiOperation("List for all transactions")
    public FebsResponse getAllTransactions(Transaction transaction) {
        return new FebsResponse().success().data(transactionService.findTransactions(transaction));
    }

    @GetMapping("transaction/list")
    @ResponseBody
    @RequiresPermissions("transaction:list")
    @ApiOperation("List for transactions")
    public FebsResponse transactionList(QueryRequest request, Transaction transaction) {
        Map<String, Object> dataTable = getDataTable(this.transactionService.findTransactions(request, transaction));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Transaction", exceptionMessage = "新增Transaction失败")
    @PostMapping("transaction")
    @ResponseBody
    @RequiresPermissions("transaction:add")
    @ApiOperation("Add a transaction")
    public FebsResponse addTransaction(@Valid Transaction transaction) {
        this.transactionService.createTransaction(transaction);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Transaction", exceptionMessage = "删除Transaction失败")
    @GetMapping("transaction/delete")
    @ResponseBody
    @RequiresPermissions("transaction:delete")
    @ApiOperation("Delete a transaction")
    public FebsResponse deleteTransaction(Transaction transaction) {
        this.transactionService.deleteTransaction(transaction);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Transaction", exceptionMessage = "修改Transaction失败")
    @PostMapping("transaction/update")
    @ResponseBody
    @RequiresPermissions("transaction:update")
    @ApiOperation("Update a transaction")
    public FebsResponse updateTransaction(Transaction transaction) {
        this.transactionService.updateTransaction(transaction);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "export excel", exceptionMessage = "导出Excel失败")
    @PostMapping("transaction/excel")
    @ResponseBody
    @RequiresPermissions("transaction:export")
    @ApiOperation("export excel")
    public void export(QueryRequest queryRequest, Transaction transaction, HttpServletResponse response) {
        List<Transaction> transactions = this.transactionService.findTransactions(queryRequest, transaction).getRecords();
        ExcelKit.$Export(Transaction.class, response).downXlsx(transactions, false);
    }
}
