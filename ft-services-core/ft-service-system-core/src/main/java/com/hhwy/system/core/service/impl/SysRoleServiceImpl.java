package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.exception.CustomException;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.datascope.annotation.DataScope;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.*;
import com.hhwy.system.core.domain.SysRoleDept;
import com.hhwy.system.core.domain.SysRoleMenu;
import com.hhwy.system.core.domain.SysUserRole;
import com.hhwy.system.core.mapper.*;
import com.hhwy.system.core.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author hhwy
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private TokenService tokenService;

    /*public void setTenantInfo(SysRole role){
        role.setTenantKey(tokenService.getTenantKey());
        Map<String, Object> params = role.getParams();
        if(params == null){
            params = new HashMap<>();
        }
        params.put("tenantRoleStatus",tokenService.getTenant().getRoleStatus());
        role.setParams(params);
    }*/

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role,tokenService.getTenantKeyListForRole());
    }

    /**
     * 根据用户ID查询权限
     *
     * @param user 用户
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(SysUser user) {
        Long userId = user.getUserId();
        SysTenant tenant = user.getTenant();
        String tenantKey = tenant.getTenantKey();

        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(tenantKey);
        if (!tenantKey.equals(Constants.MASTER_TENANT_KEY) && tenant.getRoleStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }

        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId,tenantKey,tenantKeyList);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleIdListByUserId(Long userId) {
        return roleMapper.selectRoleIdListByUserId(userId,tokenService.getTenantKey(),tokenService.getTenantKeyListForRole());
    }

    public SysRole fillInfo(SysRole sysRole){
        List<Long> menuIdList = menuMapper.selectMenuIdListByRoleId(sysRole.getRoleId(), sysRole.getMenuCheckStrictly(),tokenService.getTenantKey(),tokenService.getTenantKeyListForMenu());
        if (!CollectionUtils.isEmpty(menuIdList)) {
            Long[] ids = new Long[menuIdList.size()];
            menuIdList.toArray(ids);
            sysRole.setMenuIds(ids);
        }

        List<Long> mistyMenuIdList = menuMapper.selectMistyMenuIdListByRoleId(sysRole.getRoleId(), sysRole.getMenuCheckStrictly(),tokenService.getTenantKey(),tokenService.getTenantKeyListForMenu());
        if (!CollectionUtils.isEmpty(mistyMenuIdList)) {
            Long[] ids = new Long[mistyMenuIdList.size()];
            mistyMenuIdList.toArray(ids);
            sysRole.setMistyMenuIds(ids);
        }

        List<Long> deptIdList = deptMapper.selectDeptIdListByRoleId(sysRole.getRoleId(), sysRole.getDeptCheckStrictly(),
                tokenService.getTenantKey(),tokenService.getTenantKeyListForDept());
        if (!CollectionUtils.isEmpty(deptIdList)) {
            Long[] ids = new Long[deptIdList.size()];
            deptIdList.toArray(ids);
            sysRole.setDeptIds(ids);
            List<SysDept> sysDeptList = deptMapper.selectDeptListByIds(ids);
            sysRole.setDeptList(sysDeptList);
        }
        return sysRole;
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        SysRole sysRole = roleMapper.selectRoleById(roleId);
        return fillInfo(sysRole);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName(), tokenService.getTenantKeyListForRole());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey(), tokenService.getTenantKeyListForRole());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId,tokenService.getTenantKey());
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        role.setTenantKey(tokenService.getTenantKey());
        // 新增角色信息
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId(), tokenService.getTenantKey());
        int rows = insertRoleMenu(role);
        tokenService.setRoleAndPermissionUpdateCollection();
        return rows;
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId(), tokenService.getTenantKey());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        if (role.getMenuIds() != null) {
            for (Long menuId : role.getMenuIds()) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(role.getRoleId());
                rm.setMenuId(menuId);
                rm.setMistyFlag("0");
                rm.setTenantKey(tokenService.getTenantKey());
                list.add(rm);
            }
        }

        if (role.getMistyMenuIds() != null) {
            for (Long menuId : role.getMistyMenuIds()) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(role.getRoleId());
                rm.setMenuId(menuId);
                rm.setMistyFlag("1");
                rm.setTenantKey(tokenService.getTenantKey());
                list.add(rm);
            }
        }

        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            rd.setTenantKey(tokenService.getTenantKey());
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId, tokenService.getTenantKey());
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId, tokenService.getTenantKey());
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds, tokenService.getTenantKey());
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDept(roleIds, tokenService.getTenantKey());
        int rows = roleMapper.deleteRoleByIds(roleIds);
        if(rows>0){
            tokenService.setRoleAndPermissionUpdateCollection();
        }
        return rows;
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole,tokenService.getTenantKey());
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, String userIds) {
        Long[] userIdArr = Convert.toLongArray(userIds);
        Assert.notEmpty(userIdArr,"请先选择需要撤销的用户");
        int rows = userRoleMapper.deleteUserRoleInfos(roleId, userIdArr ,tokenService.getTenantKey());
        if (rows>0) {
            tokenService.setRoleAndPermissionUpdateCollection();
        }
        return rows;
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long roleId, String userIds) {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<>();
        for (Long userId : users) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            ur.setTenantKey(tokenService.getTenantKey());
            list.add(ur);
        }
        int rows = userRoleMapper.batchUserRole(list);
        if (rows>0) {
            tokenService.setRoleAndPermissionUpdateCollection();
        }
        return rows;
    }

}
