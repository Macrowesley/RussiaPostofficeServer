package cc.mrbird.febs.audit.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("auditView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "audit")
public class ViewController {

    @GetMapping("audit")
    @RequiresPermissions("audit:view")
    public String auditIndex(){
        return FebsUtil.view("audit/audit");
    }

}
