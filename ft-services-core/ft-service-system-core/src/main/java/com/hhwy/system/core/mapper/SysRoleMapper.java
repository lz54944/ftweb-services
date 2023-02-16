package com.hhwy.system.core.mapper;

import com.hhwy.system.api.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author hhwy
 */
public interface SysRoleMapper {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    List<SysRole> selectRoleList(@Param("role") SysRole role, @Param("tenantKeyList") List<String> tenantKeyList);

    List<SysRole> selectRoleListByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<SysRole> selectRoleAll(@Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> selectRoleIdListByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    List<String> selectSelfRoleIdListByRoleIdList(@Param("roleIdList") List<String> roleIdList, @Param("tenantKeyList") List<String> tenantKeyList);
    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserName(@Param("userName") String userName, @Param("tenantKey") String tenantKey, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    SysRole checkRoleNameUnique(@Param("roleName") String roleName, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    SysRole checkRoleKeyUnique(@Param("roleKey") String roleKey, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SysRole role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SysRole role);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);

    int realDeleteRoleByIds(List<Long> roleIdList);

    List<SysRole> selectRoleListByTenantKey(String tenantKey);
}
