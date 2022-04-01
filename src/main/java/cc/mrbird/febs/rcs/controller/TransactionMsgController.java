package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
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
 *
 * @author mrbird
 * @date 2021-04-17 14:45:32
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class TransactionMsgController extends BaseController {

    @Autowired
    ITransactionMsgService transactionMsgService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "transactionMsg")
    public String frankIndex(){
        return FebsUtil.view("transactionMsg/frank");
    }

    @GetMapping("transactionMsg")
    @ResponseBody
    //@RequiresPermissions("transactionMsg:list")
    public FebsResponse getAllTransactionMsgs(TransactionMsg frank) {
        return new FebsResponse().success().data(transactionMsgService.findTransactionMsgs(frank));
    }

    @GetMapping("transactionMsg/list")
    @ResponseBody
    @RequiresPermissions("transactionMsg:list")
    public FebsResponse frankList(QueryRequest request, TransactionMsg frank) {
        Map<String, Object> dataTable = getDataTable(this.transactionMsgService.findTransactionMsgs(request, frank));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增TransactionMsg", exceptionMessage = "新增TransactionMsg失败")
    @PostMapping("transactionMsg")
    @ResponseBody
//    @RequiresPermissions("transactionMsg:add")
    public FebsResponse addTransactionMsg(@Valid TransactionMsg frank) {
        try{
            this.transactionMsgService.createTransactionMsg(frank);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除TransactionMsg", exceptionMessage = "删除TransactionMsg失败")
    @GetMapping("transactionMsg/delete")
    @ResponseBody
    @RequiresPermissions("transactionMsg:delete")
    public FebsResponse deleteTransactionMsg(TransactionMsg frank) {
        this.transactionMsgService.deleteTransactionMsg(frank);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TransactionMsg", exceptionMessage = "修改TransactionMsg失败")
    @PostMapping("transactionMsg/update")
    @ResponseBody
    @RequiresPermissions("transactionMsg:update")
    public FebsResponse updateTransactionMsg(TransactionMsg frank) {
        this.transactionMsgService.updateTransactionMsg(frank);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改TransactionMsg", exceptionMessage = "导出Excel失败")
    @PostMapping("transactionMsg/excel")
    @ResponseBody
    @RequiresPermissions("transactionMsg:export")
    public void export(QueryRequest queryRequest, TransactionMsg frank, HttpServletResponse response) {
        List<TransactionMsg> franks = this.transactionMsgService.findTransactionMsgs(queryRequest, frank).getRecords();
        ExcelKit.$Export(TransactionMsg.class, response).downXlsx(franks, false);
    }
}
