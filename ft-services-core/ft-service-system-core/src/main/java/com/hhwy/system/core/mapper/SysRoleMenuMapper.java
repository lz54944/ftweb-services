package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author hhwy
 */
public interface SysRoleMenuMapper {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(@Param("menuId") Long menuId, @Param("tenantKey") String tenantKey);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId, @Param("tenantKey") String tenantKey);

    int deleteRoleMenuByMenuIds(@Param("menuIds") List<Long> menuIds, @Param("tenantKey") String tenantKey);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRoleMenu(@Param("ids") Long[] ids, @Param("tenantKey") String tenantKey);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
