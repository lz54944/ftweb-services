package com.hhwy.system.core.service;

import com.hhwy.system.api.domain.SysMenu;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.domain.vo.RouterVo;
import com.hhwy.system.core.domain.vo.TreeSelect;
import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author hhwy
 */
public interface ISysMenuService {
    /**
     * 根据用户查询系统菜单列表
     *
     * @param sysUser 用户
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysUser sysUser);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param sysUser 用户
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu, SysUser sysUser);

    List<SysMenu> selectMenuBarList(SysMenu menu, SysUser sysUser);

    /**
     * 根据用户ID查询权限
     *
     * @param sysUser 用户
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(SysUser sysUser);

    Set<String> selectButtonPermissionsByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuIdListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 级联删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int cascadeDeleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    String checkMenuNameUnique(SysMenu menu);

    /**
     * 校验路由是否唯一
     *
     * @return 结果
     */
    String checkRouterPathUnique(SysMenu menu);
}
