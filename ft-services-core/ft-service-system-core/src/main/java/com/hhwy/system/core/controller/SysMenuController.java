package com.hhwy.system.core.controller;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysMenu;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.service.ISysMenuService;
import com.hhwy.system.core.utils.MenuTreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, tokenService.getSysUser());
        return AjaxResult.success(menus);
    }

    /**
     * 获取菜单tree列表
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping("/treeList")
    public AjaxResult treeList(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, tokenService.getSysUser());
        List<SysMenu> menuTreeList = new MenuTreeUtils().menuList(menus);
        return AjaxResult.success(menuTreeList);
    }

    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping("/treeListExcludeTenant")
    public AjaxResult treeListExcludeTenant(SysMenu menu) {
        Map<String, Object> params = new HashMap<>();
        params.put("excludeTenant","租户");
        menu.setParams(params);
        List<SysMenu> menus = menuService.selectMenuList(menu, tokenService.getSysUser());
        List<SysMenu> menuTreeList = new MenuTreeUtils().menuList(menus);
        return AjaxResult.success(menuTreeList);
    }

    /**
     * 获取菜单tree列表
     */
    @GetMapping("/menuBarList")
    public AjaxResult menuBarList(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuBarList(menu, tokenService.getSysUser());
        List<SysMenu> menuTreeList = new MenuTreeUtils().menuList(menus);
        return AjaxResult.success(menuTreeList);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    @PreAuthorize(hasPermi = "system:menu:query")
    public AjaxResult treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, tokenService.getSysUser());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        SysUser sysUser = tokenService.getSysUser();
        List<SysMenu> menus = menuService.selectMenuList(sysUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuIdListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @PreAuthorize(hasPermi = "system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(menuService.checkRouterPathUnique(menu))) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，路由地址已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsExternalLink())
                && (!StringUtils.startsWithAny(menu.getRouterPath(), "{") && !StringUtils.startsWithAny(menu.getRouterPath(), Constants.HTTP, Constants.HTTPS))) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头，或者{变量}开头");
        }
        menu.setCreateUser(SecurityUtils.getUserName());
        return toR(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize(hasPermi = "system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(menuService.checkRouterPathUnique(menu))) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，路由地址已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsExternalLink())
                && (!StringUtils.startsWithAny(menu.getRouterPath(), "{") && !StringUtils.startsWithAny(menu.getRouterPath(), Constants.HTTP, Constants.HTTPS))) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateUser(SecurityUtils.getUserName());
        return toR(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize(hasPermi = "system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        /*if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));*/
        return toAjax(menuService.cascadeDeleteMenuById(menuId));
    }

}