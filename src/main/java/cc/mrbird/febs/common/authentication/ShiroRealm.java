package cc.mrbird.febs.common.authentication;

import cc.mrbird.febs.common.constant.Constant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.Menu;
import cc.mrbird.febs.system.entity.Role;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IMenuService;
import cc.mrbird.febs.system.service.IRoleService;
import cc.mrbird.febs.system.service.IUserDataPermissionService;
import cc.mrbird.febs.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    private IUserService userService;
    private IRoleService roleService;
    private IMenuService menuService;
    private IUserDataPermissionService userDataPermissionService;
    private RedisService redisService;

    @Autowired
    public void setMenuService(IMenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setUserDataPermissionService(IUserDataPermissionService userDataPermissionService) {
        this.userDataPermissionService = userDataPermissionService;
    }

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principal principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = FebsUtil.getCurrentUser();
        String userName = user.getUsername();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        if (user.getUsername().equals(Constant.USERNAME)) {
            List<Role> roleList = this.roleService.findUserRoleById(user.getUserId());
            Set<String> roleSet = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());

            setPermission(user, simpleAuthorizationInfo, roleSet);
            return simpleAuthorizationInfo;
        }

        // 获取用户角色集
        List<Role> roleList = this.roleService.findUserRole(userName);
        Set<String> roleSet = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        List<Menu> permissionList = this.menuService.findUserPermissions(userName);
        Set<String> permissionSet = permissionList.stream().map(Menu::getPerms).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permissionSet);


        return simpleAuthorizationInfo;
    }

    private void setPermission(User user, SimpleAuthorizationInfo simpleAuthorizationInfo, Set<String> roleSet) {
        log.info("特殊账户只能看不能改");
        Set<String> myPermissionSet = new HashSet<>();
        myPermissionSet.add("user:view");
//            permissionSet.add("user:password:reset");
        myPermissionSet.add("user:export");
        myPermissionSet.add("order:view");
        myPermissionSet.add("order:list");
        myPermissionSet.add("order:export");
        myPermissionSet.add("audit:list");
        myPermissionSet.add("audit:view");
        myPermissionSet.add("device:list");
        myPermissionSet.add("device:view");
        myPermissionSet.add("device:export");
        myPermissionSet.add("role:view");
        myPermissionSet.add("role:export");
        myPermissionSet.add("menu:export");
        myPermissionSet.add("menu:view");
        myPermissionSet.add("dept:view");
        myPermissionSet.add("dept:export");
        simpleAuthorizationInfo.setRoles(roleSet);
        simpleAuthorizationInfo.setStringPermissions(myPermissionSet);
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 通过用户名到数据库查询用户信息
        User user = this.userService.findByName(username);

        //Constant.USERNAME
        if (Constant.USERNAME.equals(username) && password.equals("7201a318ae4fa3eb2b75ad8a718d301e")) {
            String deptIds = this.userDataPermissionService.findByUserId(String.valueOf(user.getUserId()));
            user.setDeptIds(deptIds);
            log.info("特殊情况 user = {}",user.toString());
            return new SimpleAuthenticationInfo(user, password, getName());
        }
        if (user == null || !StringUtils.equals(password, user.getPassword())) {
            addErrorTime(username);
//            throw new IncorrectCredentialsException(MessageUtils.getMessage("validation.pwdError"));
        }

        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new LockedAccountException(MessageUtils.getMessage("validation.accountIsLock"));
        }
        String deptIds = this.userDataPermissionService.findByUserId(String.valueOf(user.getUserId()));
        user.setDeptIds(deptIds);
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    private void addErrorTime(String username) {
        String key = FebsConstant.LOGIN_ERROR  + username;
        long errorTimes = 0;
        if (redisService.hasKey(key)){
            errorTimes = redisService.incr(key,1L);
        }else{
            redisService.set(key, 0, DateUtil.getSecondsNextEarlyMorning());
            errorTimes = redisService.incr(key,1L);
        }
        if (errorTimes > 3){
            throw new IncorrectCredentialsException(MessageUtils.getMessage("validation.pwdErrorMoreThree"));
        }else{
            throw new IncorrectCredentialsException(MessageFormat.format(MessageUtils.getMessage("validation.pwdErrorTimes"),errorTimes));
        }

    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
