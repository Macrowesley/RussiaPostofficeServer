package cc.mrbird.febs.audit.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Controller("auditView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "audit")
public class ViewController {

    @GetMapping("audit")
    @RequiresPermissions("audit:view")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_view", isApi = false)
    public String auditIndex(){
        return FebsUtil.view("audit/audit");
    }

    @GetMapping("reject/{auditId}")
    @RequiresPermissions("audit:update")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_view", isApi = false)
    public String reject(@NotBlank @PathVariable String auditId, Model model){
        model.addAttribute("auditId", auditId);
        return FebsUtil.view("audit/reject");
    }

    /**
     * 注资列表中，查看注资审核详情
     * @param orderId
     * @param model
     * @return
     */
    @GetMapping("selectByOrderId/{orderId}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_audit_view", isApi = false)
    public String selectByOrderId(@PathVariable @NotNull @Min(1) Long orderId, Model model){
        model.addAttribute("orderId", orderId);
        return FebsUtil.view("audit/listByOrderId");
    }

}
