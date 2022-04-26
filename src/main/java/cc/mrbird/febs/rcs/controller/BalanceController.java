package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Balance;
import cc.mrbird.febs.rcs.service.IBalanceService;

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
 * 数据同步记录表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:00
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class BalanceController extends BaseController {

    @Autowired
    IBalanceService balanceService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "balance")
    public String balanceIndex(){
        return FebsUtil.view("balance/balance");
    }

    @GetMapping("balance")
    @ResponseBody
    @RequiresPermissions("balance:list")
    public FebsResponse getAllBalances(Balance balance) {
        return new FebsResponse().success().data(balanceService.findBalances(balance));
    }

    @GetMapping("balance/list")
    @ResponseBody
    @RequiresPermissions("balance:list")
    public FebsResponse balanceList(QueryRequest request, Balance balance) {
        Map<String, Object> dataTable = getDataTable(this.balanceService.findBalances(request, balance));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Balance", exceptionMessage = "新增Balance失败")
    @PostMapping("balance")
    @ResponseBody
    @RequiresPermissions("balance:add")
    public FebsResponse addBalance(@Valid Balance balance) {
        this.balanceService.createBalance(balance);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Balance", exceptionMessage = "删除Balance失败")
    @GetMapping("balance/delete")
    @ResponseBody
    @RequiresPermissions("balance:delete")
    public FebsResponse deleteBalance(Balance balance) {
        this.balanceService.deleteBalance(balance);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Balance", exceptionMessage = "修改Balance失败")
    @PostMapping("balance/update")
    @ResponseBody
    @RequiresPermissions("balance:update")
    public FebsResponse updateBalance(Balance balance) {
        this.balanceService.updateBalance(balance);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Balance", exceptionMessage = "导出Excel失败")
    @PostMapping("balance/excel")
    @ResponseBody
    @RequiresPermissions("balance:export")
    public void export(QueryRequest queryRequest, Balance balance, HttpServletResponse response) {
        List<Balance> balances = this.balanceService.findBalances(queryRequest, balance).getRecords();
        //ExcelKit.$Export(Balance.class, response).downXlsx(balances, false);
    }
}
