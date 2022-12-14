package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.authentication.ShiroRealm;
import cc.mrbird.febs.common.constant.Constant;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.system.entity.Menu;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.mapper.MenuMapper;
import cc.mrbird.febs.system.service.IMenuService;
import cc.mrbird.febs.system.service.IRoleMenuService;
import cc.mrbird.febs.system.service.IUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    IRoleMenuService roleMenuService;
    @Autowired
    ShiroRealm shiroRealm;
    @Autowired
    IUserService userService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public List<Menu> findUserPermissions(String username) {
        return this.baseMapper.findUserPermissions(username);
    }

    @Override
    public MenuTree<Menu> findUserMenus(String username) {
        if (username.equals(Constant.USERNAME)){
            User user = userService.getById(FebsUtil.getCurrentUser().getUserId());
            username = user.getUsername();
        }
        List<Menu> menus = this.baseMapper.findUserMenus(username);
        List<MenuTree<Menu>> trees = this.convertMenus(menus);
        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public MenuTree<Menu> findMenus(Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //???????????????????????????title??????
        if (StringUtils.isNotBlank(menu.getMenuEnglishName())) {
            if("zh_CN".equals(RequestContextUtils.getLocale(request).toString())){
                queryWrapper.lambda().like(Menu::getMenuChineseName, menu.getMenuChineseName());
            }else if("ru_RUS".equals(RequestContextUtils.getLocale(request).toString())){
                queryWrapper.lambda().like(Menu::getMenuRussianName, menu.getMenuRussianName());
            }else{
                queryWrapper.lambda().like(Menu::getMenuEnglishName, menu.getMenuEnglishName());
            }
        }
        queryWrapper.lambda().orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(queryWrapper);
        List<MenuTree<Menu>> trees = this.convertMenus(menus);

        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public List<Menu> findMenuList(Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuEnglishName())) {
            if("zh_CN".equals(RequestContextUtils.getLocale(request).toString())){
                queryWrapper.lambda().like(Menu::getMenuChineseName, menu.getMenuChineseName());
            }else if("ru_RUS".equals(RequestContextUtils.getLocale(request).toString())){
                queryWrapper.lambda().like(Menu::getMenuRussianName, menu.getMenuRussianName());
            }else{
                queryWrapper.lambda().like(Menu::getMenuEnglishName, menu.getMenuEnglishName());
            }
        }
        queryWrapper.lambda().orderByAsc(Menu::getMenuId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(Menu menu) {
        menu.setCreateTime(new Date());
        this.setMenu(menu);
        this.baseMapper.insert(menu);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        menu.setModifyTime(new Date());
        this.setMenu(menu);
        this.baseMapper.updateById(menu);

        shiroRealm.clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenus(String menuIds) {
        String[] menuIdsArray = menuIds.split(StringPool.COMMA);
        this.delete(Arrays.asList(menuIdsArray));

        shiroRealm.clearCache();
    }

    private List<MenuTree<Menu>> convertMenus(List<Menu> menus) {
        List<MenuTree<Menu>> trees = new ArrayList<>();
        menus.forEach(menu -> {
            MenuTree<Menu> tree = new MenuTree<>();
            tree.setId(String.valueOf(menu.getMenuId()));
            tree.setParentId(String.valueOf(menu.getParentId()));

            //???????????????????????????title??????
            if("zh_CN".equals(RequestContextUtils.getLocale(request).toString())){
                tree.setTitle(menu.getMenuChineseName());
            }else if("ru_RUS".equals(RequestContextUtils.getLocale(request).toString())){
                tree.setTitle(menu.getMenuRussianName());
            }else{
                tree.setTitle(menu.getMenuEnglishName());
            }
            tree.setIcon(menu.getIcon());
            tree.setHref(menu.getUrl());
            tree.setData(menu);
            trees.add(tree);
        });
        return trees;
    }

    private void setMenu(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(Menu.TOP_NODE);
        }
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
    }

    private void delete(List<String> menuIds) {
        List<String> list = new ArrayList<>(menuIds);
        removeByIds(menuIds);

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getParentId, menuIds);
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getMenuId())));
            list.addAll(menuIdList);
            this.roleMenuService.deleteRoleMenusByMenuId(list);
            this.delete(menuIdList);
        } else {
            this.roleMenuService.deleteRoleMenusByMenuId(list);
        }
    }
}
