package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysRoleDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author hhwy
 */
public interface SysRoleDeptMapper {
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleDeptByRoleId(@Param("roleId") Long roleId, @Param("tenantKey") String tenantKey);

    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRoleDept(@Param("ids") Long[] ids, @Param("tenantKey") String tenantKey);

    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int selectCountRoleDeptByDeptId(@Param("roleId") Long deptId, @Param("tenantKey") String tenantKey);

    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    int batchRoleDept(List<SysRoleDept> roleDeptList);
}
