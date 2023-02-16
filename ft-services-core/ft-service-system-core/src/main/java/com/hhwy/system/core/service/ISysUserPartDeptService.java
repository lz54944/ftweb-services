package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysUserPartDept;
import java.util.List;

/**
 * 兼职部门Service接口
 *
 * @author jzq
 * @date 2021-07-06
 */
public interface ISysUserPartDeptService {
    /**
     * 查询兼职部门
     *
     * @param userId 兼职部门ID
     * @return 兼职部门
     */
    SysUserPartDept selectSysUserPartDeptById(Long userId);

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
     * 修改兼职部门
     *
     * @param sysUserPartDept 兼职部门
     * @return 结果
     */
    int updateSysUserPartDept(SysUserPartDept sysUserPartDept);

    /**
     * 批量删除兼职部门
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysUserPartDeptByIds(String ids);

    /**
     * 删除兼职部门信息
     *
     * @param userId 兼职部门ID
     * @return 结果
     */
    int deleteSysUserPartDeptByUserId(Long userId);
}
