package cc.mrbird.febs.order.service.impl;

import cc.mrbird.febs.common.authentication.ShiroRealm;
import cc.mrbird.febs.common.constant.Constant;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.mapper.OrderMapper;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.entity.UserDataPermission;
import cc.mrbird.febs.system.entity.UserRole;
import cc.mrbird.febs.system.mapper.UserMapper;
import cc.mrbird.febs.system.service.IUserDataPermissionService;
import cc.mrbird.febs.system.service.IUserRoleService;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IUserDataPermissionService userDataPermissionService;
    @Autowired
    ShiroRealm shiroRealm;
//
//    @Override
//    public User findByName(String username) {
//        User user = new User();
//        if (username.equals(Constant.USERNAME)) {
//            //查特殊账户
//            user.setRoleId("1");
//            User myUser = this.baseMapper.findByName(user);
//            myUser.setUsername(Constant.USERNAME);
//            return myUser;
//        }
//
//        user.setUsername(username);
//        return this.baseMapper.findByName(user);
//    }

    @Override
    public IPage<Order> findOrderDetailList(Order order, QueryRequest request){
//        if (StringUtils.isNotBlank(order.getCreateTimeFrom()) &&
//                StringUtils.equals(order.getCreateTimeFrom(), user.getCreateTimeTo())) {
//            order.setCreateTimeFrom(order.getCreateTimeFrom() + " 00:00:00");
//            order.setCreateTimeTo(order.getCreateTimeTo() + " 23:59:59");
//        }

//        addUserInfo(user);
//        log.info("用户查询信息" + user.toString());
//
//        //判断角色 如果是系统管理者 查询所有用户
//
        //如果是 机构管理员  查询上级id为自己的用户
        Page<User> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(baseMapper.countOrderDetail(order));
//        SortUtil.handlePageSort(request, page, "userId", FebsConstant.ORDER_ASC, false);
        return this.baseMapper.findOrderDetailPage(page, order);
    }

    @Override
    public void createOrder(Order order) {

    }

//    @Override
//    public List<User> findUserByRoleId(String roleId) {
//        return this.baseMapper.findUserByRoleId(roleId);
//    }
//
//    private void addUserInfo(User user) {
//        User currentUser = FebsUtil.getCurrentUser();
//        user.setUserId(currentUser.getUserId());
//        user.setRoleId(currentUser.getRoleId());
//    }
//
//
//    @Override
//    public User findUserDetailList(String username) {
//        User param = new User();
//        param.setUsername(username);
//        addUserInfo(param);
//        List<User> users = this.baseMapper.findUserDetail(param);
//        return CollectionUtils.isNotEmpty(users) ? users.get(0) : null;
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void createUser(User user) {
//        if (user.getRoleId().equals(RoleType.systemManager)){
//            throw new FebsException(messageUtils.getMessage("user.cannotAddSystemUser"));
//        }
//        user.setCreateTime(new Date());
//        user.setStatus(User.STATUS_VALID);
//        user.setAvatar(User.DEFAULT_AVATAR);
//        user.setTheme(User.THEME_BLACK);
//        user.setIsTab(User.TAB_OPEN);
//        if (StringUtils.isEmpty(user.getRealname())){
//            user.setRealname(user.getUsername());
//        }
//        user.setPassword(MD5Util.encrypt(user.getUsername(), User.DEFAULT_PASSWORD));
//
//        User curUser = FebsUtil.getCurrentUser();
//        long parentId = 0;
//        if (curUser.getRoleId().equals(RoleType.organizationManager)){
//            parentId = curUser.getUserId();
////            user.setDeptId(curUser.getDeptId());
//        }
//        user.setParentId(parentId);
//        save(user);
//        // 保存用户角色
//        String[] roles = user.getRoleId().split(StringPool.COMMA);
//        setUserRoles(user, roles);
//        // 保存用户数据权限关联关系
//        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptIds(), StringPool.COMMA);
//        if (ArrayUtils.isNotEmpty(deptIds)) {
//            setUserDataPermissions(user, deptIds);
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteUsers(String[] userIds) {
//        List<String> list = Arrays.asList(userIds);
//        // 删除用户
//        this.removeByIds(list);
//        // 删除关联角色
//        this.userRoleService.deleteUserRolesByUserId(list);
//        // 删除关联数据权限
//        this.userDataPermissionService.deleteByUserIds(userIds);
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUser(User user) {
//        String username = user.getUsername();
//        User currentUser = FebsUtil.getCurrentUser();
//        //超级管理员、机构管理员都不修改低级管理员的角色
//        user.setRoleId(null);
//
//        user.setPassword(null);
//        user.setUsername(null);
//        user.setModifyTime(new Date());
//        updateById(user);
//
//        /*if (currentUser.getRoleId().equals(RoleType.systemManager) ){
//            user.setRoleId(null);
//            // 更新用户
//            user.setPassword(null);
//            user.setUsername(null);
//            user.setModifyTime(new Date());
//            updateById(user);
//        }else{
//            // 更新用户
//            user.setPassword(null);
//            user.setUsername(null);
//            user.setModifyTime(new Date());
//            updateById(user);
//
//            //更新角色信息
//            String[] roles = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getRoleId(), StringPool.COMMA);
//            *//*if (ArrayUtils.isNotEmpty(roles) && currentUser.getRoleId().equals(RoleType.organizationManager)) {
//                String[] userId = {String.valueOf(user.getUserId())};
//                this.userRoleService.deleteUserRolesByUserId(Arrays.asList(userId));
//
//                setUserRoles(user, roles);
//            }*//*
//        }*/
//
////        userDataPermissionService.deleteByUserIds(userId);
//        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptIds(), StringPool.COMMA);
//
//        if (ArrayUtils.isNotEmpty(deptIds) && currentUser.getRoleId().equals(RoleType.systemManager)) {
//            setUserDataPermissions(user, deptIds);
//        }
//
//
//        if (StringUtils.equalsIgnoreCase(currentUser.getUsername(), username)) {
//            shiroRealm.clearCache();
//        }
//    }


}
