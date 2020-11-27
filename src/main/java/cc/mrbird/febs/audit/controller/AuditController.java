package cc.mrbird.febs.audit.controller;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.utils.StatusUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import lombok.val;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 审核表 Controller
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("audit")
public class AuditController extends BaseController {

    private final IAuditService auditService;

    @GetMapping("list")
    @RequiresPermissions("audit:list")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_audit")
    public FebsResponse auditList(QueryRequest request, Audit audit) {
        IPage<Audit> pageInfo = this.auditService.findPageAudits(request, audit);
        pageInfo.getRecords().stream().forEach(bean -> {
            bean.setBtnList(StatusUtils.getAuditBtnMapList(bean.getStatus()));
        });
        Map<String, Object> dataTable = getDataTable(pageInfo);
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 根据一个orderId获取列表
     * @param orderId
     * @return
     */
    @GetMapping("selectByOrderId/{orderId}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_audit")
    public FebsResponse selectByOrderId(@NotBlank @PathVariable String orderId, QueryRequest request) {
        Audit audit = new Audit();
        audit.setOrderId(Long.valueOf(orderId));
        Map<String, Object> dataTable = getDataTable(this.auditService.findPageAudits(request, audit));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "修改审核", exceptionMessage = "{audit.operation.editError}")
    @PostMapping("update")
    @RequiresPermissions("audit:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_audit_audit")
    public FebsResponse updateAudit(Audit audit) {
        this.auditService.updateAudit(audit);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "审核通过", exceptionMessage = "{audit.operation.passError}")
    @PostMapping("pass/{auditId}")
    @RequiresPermissions("audit:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_audit_audit")
    public FebsResponse passAudit(@NotBlank @PathVariable String auditId) {
        this.auditService.auditOneSuccess(auditId);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "审核不通过", exceptionMessage = "{audit.operation.updateError}")
    @PostMapping("noPass")
    @RequiresPermissions("audit:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_audit_audit")
    public FebsResponse noPassAudit(Audit audit) {
        this.auditService.auditOneFail(String.valueOf(audit.getAuditId()), audit.getCheckRemark());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "获取审核状态列表", exceptionMessage = "{audit.operation.listError}")
    @GetMapping("selectStatus")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_audit")
    public FebsResponse selectStatus() {
        return new FebsResponse().success().data(StatusUtils.getAuditStatusList());
    }


/*    @ControllerEndpoint(operation = "修改Audit", exceptionMessage = "导出Excel失败")
    @PostMapping("audit/excel")
    @RequiresPermissions("audit:export")
    public void export(QueryRequest queryRequest, Audit audit, HttpServletResponse response) {
        List<Audit> audits = this.auditService.findPageAudits(queryRequest, audit).getRecords();
        ExcelKit.$Export(Audit.class, response).downXlsx(audits, false);
    }*/
}
