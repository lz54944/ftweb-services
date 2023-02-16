package com.hhwy.system.core.mapper;

import com.hhwy.system.api.domain.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author hhwy
 */
public interface SysMenuMapper {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(@Param("menu") SysMenu menu, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    //List<String> selectMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(@Param("menu") SysMenu menu, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    List<SysMenu> selectMenuBarListByUserId(@Param("menu") SysMenu menu, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    List<String> selectButtonPermissionsByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    //List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Long> selectMenuIdListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    List<Long> selectMistyMenuIdListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

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
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
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

    int realDeleteMenuByIds(List<Long> menuIdList);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 校验路由是否唯一
     *
     * @return 结果
     */
    SysMenu checkRouterPathUnique(@Param("routerPath") String routerPath, @Param("tenantKeyList") List<String> tenantKeyList);

    List<SysMenu> selectMenuListByTenantKey(String tenantKey);
}
