package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Customer;
import cc.mrbird.febs.rcs.service.ICustomerService;
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
 * 用户表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:59
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final ICustomerService customerService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "customer")
    public String customerIndex(){
        return FebsUtil.view("customer/customer");
    }

    @GetMapping("customer")
    @ResponseBody
    @RequiresPermissions("customer:list")
    public FebsResponse getAllCustomers(Customer customer) {
        return new FebsResponse().success().data(customerService.findCustomers(customer));
    }

    @GetMapping("customer/list")
    @ResponseBody
    @RequiresPermissions("customer:list")
    public FebsResponse customerList(QueryRequest request, Customer customer) {
        Map<String, Object> dataTable = getDataTable(this.customerService.findCustomers(request, customer));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Customer", exceptionMessage = "新增Customer失败")
    @PostMapping("customer")
    @ResponseBody
    @RequiresPermissions("customer:add")
    public FebsResponse addCustomer(@Valid Customer customer) {
        this.customerService.createCustomer(customer);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Customer", exceptionMessage = "删除Customer失败")
    @GetMapping("customer/delete")
    @ResponseBody
    @RequiresPermissions("customer:delete")
    public FebsResponse deleteCustomer(Customer customer) {
        this.customerService.deleteCustomer(customer);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Customer", exceptionMessage = "修改Customer失败")
    @PostMapping("customer/update")
    @ResponseBody
    @RequiresPermissions("customer:update")
    public FebsResponse updateCustomer(Customer customer) {
        this.customerService.updateCustomer(customer);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Customer", exceptionMessage = "导出Excel失败")
    @PostMapping("customer/excel")
    @ResponseBody
    @RequiresPermissions("customer:export")
    public void export(QueryRequest queryRequest, Customer customer, HttpServletResponse response) {
        List<Customer> customers = this.customerService.findCustomers(queryRequest, customer).getRecords();
        ExcelKit.$Export(Customer.class, response).downXlsx(customers, false);
    }
}
