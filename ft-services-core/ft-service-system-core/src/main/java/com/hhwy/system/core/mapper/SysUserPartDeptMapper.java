package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysUserPartDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 兼职部门Mapper接口
 *
 * @author jzq
 * @date 2021-07-06
 */
public interface SysUserPartDeptMapper {
    /**
     * 查询兼职部门
     *
     * @param userId 兼职部门ID
     * @return 兼职部门
     */
    SysUserPartDept selectSysUserPartDeptById(@Param("userId") Long userId, @Param("tenantKey") String tenantKey);

    /**
     * 查询兼职部门列表
     *
     * @param sysUserPartDept 兼职部门
     * @return 兼职部门集合
     */
    List<SysUserPartDept> selectSysUserPartDeptList(SysUserPartDept sysUserPartDept);

    /**
     * 新增兼职部门
     *
     * @param sysUserPartDept 兼职部门
     * @return 结果
     */
    int insertSysUserPartDept(SysUserPartDept sysUserPartDept);

    /**
     * 批量新增
     *
     * @return 结果
     */
    int batchInsertSysUserPartDept(List<SysUserPartDept> sysUserPartDeptList);

    /**
     * 修改兼职部门
     *
     * @param sysUserPartDept 兼职部门
     * @return 结果
     */
    int updateSysUserPartDept(SysUserPartDept sysUserPartDept);

    /**
     * 删除兼职部门
     *
     * @param userId 兼职部门ID
     * @return 结果
     */
    int deleteSysUserPartDeptByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey);

    /**
     * 批量删除兼职部门
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    int deleteSysUserPartDeptByIds(@Param("userIds") String[] userIds, @Param("tenantKey") String tenantKey);
}
