package cc.mrbird.febs.audit.controller;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public FebsResponse auditList(QueryRequest request, Audit audit) {
        Map<String, Object> dataTable = getDataTable(this.auditService.findPageAudits(request, audit));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "删除Audit", exceptionMessage = "删除Audit失败")
    @GetMapping("audit/delete")
    @RequiresPermissions("audit:delete")
    public FebsResponse deleteAudit(Audit audit) {
        this.auditService.deleteAudit(audit);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Audit", exceptionMessage = "修改Audit失败")
    @PostMapping("audit/update")
    @RequiresPermissions("audit:update")
    public FebsResponse updateAudit(Audit audit) {
        this.auditService.updateAudit(audit);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Audit", exceptionMessage = "导出Excel失败")
    @PostMapping("audit/excel")
    @RequiresPermissions("audit:export")
    public void export(QueryRequest queryRequest, Audit audit, HttpServletResponse response) {
        List<Audit> audits = this.auditService.findPageAudits(queryRequest, audit).getRecords();
        ExcelKit.$Export(Audit.class, response).downXlsx(audits, false);
    }
}
