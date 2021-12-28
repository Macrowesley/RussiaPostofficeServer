package cc.mrbird.febs.job.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.job.entity.Job;
import cc.mrbird.febs.job.service.IJobService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;


@Controller("jobView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "job")
@RequiredArgsConstructor
@ApiIgnore
public class ViewController {

    private final IJobService jobService;

    @GetMapping("job")
    @RequiresPermissions("job:view")
    public String online() {
        return FebsUtil.view("job/job");
    }

    @GetMapping("job/add")
    @RequiresPermissions("job:add")
    public String jobAdd() {
        return FebsUtil.view("job/jobAdd");
    }

    @GetMapping("job/update/{jobId}")
    @RequiresPermissions("job:update")
    public String jobUpdate(@NotBlank(message = "{required}") @PathVariable Long jobId, Model model) {
        Job job = jobService.findJob(jobId);
        model.addAttribute("job", job);
        return FebsUtil.view("job/jobUpdate");
    }

    @GetMapping("log")
    @RequiresPermissions("job:log:view")
    public String log() {
        return FebsUtil.view("job/jobLog");
    }

//    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/update/{username}")
//    @RequiresPermissions("user:update")
//    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_view", isApi = false)
//    public String systemUserUpdate(@PathVariable String username, Model model) {
//        resolveUserModel(username, model, false);
//        model.addAttribute("roleId", FebsUtil.getCurrentUser().getRoleId());
//        return FebsUtil.view("system/user/userUpdate");
//    }

}
