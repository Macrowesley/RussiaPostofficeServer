package cc.mrbird.febs.audit.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

@Controller("auditView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "audit")
public class ViewController {

    @GetMapping("audit")
    @RequiresPermissions("audit:view")
    public String auditIndex(){
        return FebsUtil.view("audit/audit");
    }

    @GetMapping("reject/{auditId}")
    @RequiresPermissions("audit:update")
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
    public String selectByOrderId(@NotBlank @PathVariable String orderId, Model model){
        model.addAttribute("orderId", orderId);
        return FebsUtil.view("audit/listByOrderId");
    }

}
