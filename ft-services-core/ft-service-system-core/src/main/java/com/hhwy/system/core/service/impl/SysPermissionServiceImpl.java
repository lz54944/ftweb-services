package com.hhwy.system.core.service.impl;

import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.service.ISysMenuService;
import com.hhwy.system.core.service.ISysPermissionService;
import com.hhwy.system.core.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SysPermissionServiceImpl implements ISysPermissionService {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param sysUser 用户
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUser sysUser) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (sysUser.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(sysUser));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param sysUser 用户
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser sysUser) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (sysUser.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(sysUser));
        }
        return perms;
    }

    /**
     * 获取按钮权限
     *
     * @param sysUser 用户
     * @return 按钮权限信息
     */
    @Override
    public Set<String> selectButtonPermissionsByUserId(SysUser sysUser) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (sysUser.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectButtonPermissionsByUserId(sysUser.getUserId()));
        }
        return perms;
    }
}
