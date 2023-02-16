package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.text.Convert;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.core.domain.SysUserPartDept;
import com.hhwy.system.core.mapper.SysUserPartDeptMapper;
import com.hhwy.system.core.service.ISysUserPartDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 兼职部门Service业务层处理
 *
 * @author jzq
 * @date 2021-07-06
 */
@Service
public class SysUserPartDeptServiceImpl implements ISysUserPartDeptService {
    @Autowired
    private SysUserPartDeptMapper sysUserPartDeptMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询兼职部门
     *
     * @param userId 兼职部门ID
     * @return 兼职部门
     */
    @Override
    public SysUserPartDept selectSysUserPartDeptById(Long userId) {
        return sysUserPartDeptMapper.selectSysUserPartDeptById(userId, tokenService.getTenantKey());
    }

    /**
     * 查询兼职部门列表
     *
     * @param sysUserPartDept 兼职部门
     * @return 兼职部门
     */
    @Override
    public List<SysUserPartDept> selectSysUserPartDeptList(SysUserPartDept sysUserPartDept) {
        sysUserPartDept.setTenantKey(tokenService.getTenantKey());
        return sysUserPartDeptMapper.selectSysUserPartDeptList(sysUserPartDept);
    }

    /**
     * 新增兼职部门
     *
     * @param sysUserPartDept 兼职部门
     * @return 结果
     */
    @Override
    public int insertSysUserPartDept(SysUserPartDept sysUserPartDept) {
        sysUserPartDept.setTenantKey(tokenService.getTenantKey());
        return sysUserPartDeptMapper.insertSysUserPartDept(sysUserPartDept);
    }

    /**
     * 修改兼职部门
     *
     * @param sysUserPartDept 兼职部门
     * @return 结果
     */
    @Override
    public int updateSysUserPartDept(SysUserPartDept sysUserPartDept) {
        sysUserPartDept.setTenantKey(tokenService.getTenantKey());
        return sysUserPartDeptMapper.updateSysUserPartDept(sysUserPartDept);
    }

    /**
     * 删除兼职部门对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysUserPartDeptByIds(String ids) {
        return sysUserPartDeptMapper.deleteSysUserPartDeptByIds(Convert.toStrArray(ids), tokenService.getTenantKey());
    }

    /**
     * 删除兼职部门信息
     *
     * @param userId 兼职部门ID
     * @return 结果
     */
    @Override
    public int deleteSysUserPartDeptByUserId(Long userId) {
        return sysUserPartDeptMapper.deleteSysUserPartDeptByUserId(userId, tokenService.getTenantKey());
    }
}
