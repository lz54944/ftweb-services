package com.hhwy.system.core.service;

import com.hhwy.system.api.domain.SysUser;
import java.util.Set;

public interface ISysPermissionService {
    /**
     * 获取角色数据权限
     *
     * @param sysUser 用户
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser sysUser);

    /**
     * 获取菜单数据权限
     *
     * @param sysUser 用户
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser sysUser);

    Set<String> selectButtonPermissionsByUserId(SysUser sysUser);
}
