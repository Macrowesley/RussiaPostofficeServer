package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.system.entity.Menu;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IMenuService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("menu")
@ApiIgnore
public class MenuController extends BaseController {

    private final IMenuService menuService;

    @GetMapping("{username}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public FebsResponse getUserMenus(@NotBlank(message = "{required}") @PathVariable String username) throws FebsException {
        User currentUser = getCurrentUser();
        if (!StringUtils.equalsIgnoreCase(username, currentUser.getUsername())) {
            throw new FebsException(MessageUtils.getMessage("menu.noPamission"));
        }
        MenuTree<Menu> userMenus = this.menuService.findUserMenus(username);
        return new FebsResponse().data(userMenus);
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "{menu.selectTreeFail}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public FebsResponse getMenuTree(Menu menu) {
        MenuTree<Menu> menus = this.menuService.findMenus(menu);
        return new FebsResponse().success().data(menus.getChilds());
    }

    @PostMapping
    @RequiresPermissions("menu:add")
    @ControllerEndpoint(operation = "新增菜单/按钮", exceptionMessage = "{menu.addFail}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public FebsResponse addMenu(@Valid Menu menu) {
        this.menuService.createMenu(menu);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{menuIds}")
    @RequiresPermissions("menu:delete")
    @ControllerEndpoint(operation = "删除菜单/按钮", exceptionMessage = "{menu.deleteFail}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public FebsResponse deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        this.menuService.deleteMenus(menuIds);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("menu:update")
    @ControllerEndpoint(operation = "修改菜单/按钮", exceptionMessage = "{menu.editFail}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public FebsResponse updateMenu(@Valid Menu menu) {
        this.menuService.updateMenu(menu);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("menu:export")
    @ControllerEndpoint(exceptionMessage = "{excelFail}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_system_Menu")
    public void export(Menu menu, HttpServletResponse response) {
        List<Menu> menus = this.menuService.findMenuList(menu);
        ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
    }
}
