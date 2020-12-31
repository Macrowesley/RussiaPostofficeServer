package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@ApiIgnore
public class UserController extends BaseController {

    private final IUserService userService;

    @GetMapping("{username}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public User getUser(@NotBlank(message = "{required}") @PathVariable String username) {

        return this.userService.findUserDetailList(username);
    }

    @GetMapping("check/{username}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username, String userId) {
        return this.userService.findByName(username) == null || StringUtils.isNotBlank(userId);
    }

    @GetMapping("list")
    @RequiresPermissions("user:view")
    @ControllerEndpoint(operation = "用户列表", exceptionMessage = "{user.operation.listError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse userList(User user, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.userService.findUserDetailList(user, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("deptUserList")
    @ControllerEndpoint(operation = "获取机构用户列表", exceptionMessage = "{user.operation.listError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse deptUserList() {
        List<User> users = this.userService.findUserByRoleId(RoleType.organizationManager);
        return new FebsResponse().success().data(users);
    }

    @PostMapping
    @RequiresPermissions("user:add")
    @ControllerEndpoint(operation = "新增用户", exceptionMessage = "{user.operation.addError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse addUser(@Valid User user) {
        this.userService.createUser(user);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{userIds}")
    @RequiresPermissions("user:delete")
    @ControllerEndpoint(operation = "删除用户", exceptionMessage = "{user.operation.delError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        /*String[] ids = userIds.split(StringPool.COMMA);
        this.userService.deleteUsers(ids);*/
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("user:update")
    @ControllerEndpoint(operation = "修改用户", exceptionMessage = "{user.operation.editError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse updateUser(@Valid User user) {
        if (user.getUserId() == null) {
            throw new FebsException("用户ID为空");
        }
        this.userService.updateUser(user);
        return new FebsResponse().success();
    }

    @PostMapping("password/reset/{usernames}")
    @RequiresPermissions("user:password:reset")
    @ControllerEndpoint(exceptionMessage = "{user.operation.resetError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse resetPassword(@NotBlank(message = "{required}") @PathVariable String usernames) {
        String[] usernameArr = usernames.split(StringPool.COMMA);
        this.userService.resetPassword(usernameArr);
        return new FebsResponse().success();
    }

    @PostMapping("password/update")
    @ControllerEndpoint(exceptionMessage = "{user.operation.editPwdError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse updatePassword(
            @NotBlank(message = "{required}") String oldPassword,
            @NotBlank(message = "{required}") String newPassword) {
        User user = getCurrentUser();
        if (!StringUtils.equals(user.getPassword(), MD5Util.encrypt(user.getUsername(), oldPassword))) {
            throw new FebsException(MessageUtils.getMessage("user.operation.oldPwdError"));
        }
        userService.updatePassword(user.getUsername(), newPassword);
        return new FebsResponse().success();
    }

    @GetMapping("avatar/{image}")
    @ControllerEndpoint(exceptionMessage = "{user.operation.editAvageError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse updateAvatar(@NotBlank(message = "{required}") @PathVariable String image) {
        User user = getCurrentUser();
        this.userService.updateAvatar(user.getUsername(), image);
        return new FebsResponse().success();
    }

    @PostMapping("theme/update")
    @ControllerEndpoint(exceptionMessage = "{user.operation.editConfigError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse updateTheme(String theme, String isTab) {
        User user = getCurrentUser();
        this.userService.updateTheme(user.getUsername(), theme, isTab);
        return new FebsResponse().success();
    }

    @PostMapping("profile/update")
    @ControllerEndpoint(exceptionMessage = "{user.operation.editUserInfoError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public FebsResponse updateProfile(User user) throws FebsException {
        User currentUser = getCurrentUser();
        user.setUserId(currentUser.getUserId());
        this.userService.updateProfile(user);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("user:export")
    @ControllerEndpoint(exceptionMessage = "{user.operation.exportError}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_User")
    public void export(QueryRequest queryRequest, User user, HttpServletResponse response) {
        List<User> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
        ExcelKit.$Export(User.class, response).downXlsx(users, false);
    }
}
