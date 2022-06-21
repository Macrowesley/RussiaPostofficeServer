package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.Constant;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.license.LicenseVerifyUtils;
import cc.mrbird.febs.common.service.ValidateCodeService;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.monitor.entity.LoginLog;
import cc.mrbird.febs.monitor.service.ILoginLogService;
import cc.mrbird.febs.rcs.api.CheckUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequiredArgsConstructor
@Api(description = "Implement login, regist and captcha functions")
public class LoginController extends BaseController {

    @Autowired
    IUserService userService;
    @Autowired
    ValidateCodeService validateCodeService;
    @Autowired
    ILoginLogService loginLogService;

    @PostMapping("login")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_login")
    @ApiOperation("Log in and save login logs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "password", defaultValue = ""),
            @ApiImplicitParam(name = "verifyCode", value = "verifyCode", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = MD5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);

        if (Constant.USERNAME.equals(username)){
            return new FebsResponse().success();
        }
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }

    @PostMapping("regist")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_login")
    @ApiOperation("regist new user")
    @ApiIgnore
    public FebsResponse regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws FebsException {
        User user = userService.findByName(username);
        if (user != null) {
            throw new FebsException("该用户名已存在");
        }
        this.userService.regist(username, password);
        return new FebsResponse().success();
    }

    @GetMapping("index/{username}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_login")
    @ApiOperation("Obtain system access records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Long.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        // 更新登录时间
        this.userService.updateLoginTime(username);
        Map<String, Object> data = new HashMap<>(5);
        // 获取系统访问记录
        Long totalVisitCount = this.loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = this.loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = this.loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().success().data(data);
    }

    @GetMapping("images/captcha")
    @ApiOperation("images captcha")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_login")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, FebsException {
        validateCodeService.create(request, response);
    }
}
