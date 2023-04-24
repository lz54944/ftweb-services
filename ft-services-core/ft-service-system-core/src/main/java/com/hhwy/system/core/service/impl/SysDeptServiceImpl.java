package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.exception.CustomException;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.datascope.annotation.DataScope;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysDept;
import com.hhwy.system.api.domain.SysRole;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.domain.vo.TreeSelect;
import com.hhwy.system.core.mapper.SysDeptMapper;
import com.hhwy.system.core.mapper.SysRoleMapper;
import com.hhwy.system.core.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author hhwy
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private TokenService tokenService;

    /*public void setTenantInfo(SysDept dept){
        dept.setTenantKey(tokenService.getTenantKey());
        Map<String, Object> params = dept.getParams();
        if(params == null){
            params = new HashMap<>();
        }
        params.put("tenantDeptStatus",tokenService.getTenant().getDeptStatus());
        dept.setParams(params);
    }*/

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept, tokenService.getTenantKeyListForDept());
    }

    @Override
    @DataScope(deptAlias = "d")
    public int selectDeptListCount(SysDept dept) {
        return deptMapper.selectDeptListCount(dept, tokenService.getTenantKeyListForDept());
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectOneLevelDeptList(SysDept dept) {
        return deptMapper.selectOneLevelDeptList(dept, tokenService.getTenantKeyListForDept());
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectChildrenDeptList(SysDept dept) {
        return deptMapper.selectChildrenDeptList(dept, tokenService.getTenantKeyListForDept());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDept dept = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptIdListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptIdListByRoleId(roleId, role.getDeptCheckStrictly(),
                tokenService.getTenantKey(), tokenService.getTenantKeyListForDept());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        SysDept dept = deptMapper.selectDeptById(deptId);
        if (dept.getParentId()!=null) {
            SysDept parentDept = deptMapper.selectDeptById(dept.getParentId());
            if (parentDept!=null) {
                dept.setParentName(parentDept.getDeptName());
                dept.setParentDept(parentDept);
            }
        }
        return dept;
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId(),tokenService.getTenantKeyListForDept());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        dept.setTenantKey(tokenService.getTenantKey());
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (info != null && !UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        if (info != null) {
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        } else {
            dept.setAncestors("0");
        }
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (dept.getAncestors() != null && UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 获取当前登录人所在部门及下级部门（嵌套列表）
     * @return
     */
    @Override
    public List<SysDept> getLoginUserNestDept() {
        List<SysDept> sysDeptList = new ArrayList<>();
        SysUser sysUser = tokenService.getSysUser();
        //所有部门集合
        List<SysDept> deptListAll = this.selectDeptList(new SysDept());

        if(sysUser.isAdmin()){//当前登录人是超级管理员
            for (SysDept dept : deptListAll) {
                if(dept.getParentId() == null){
                    this.getChildren(dept,deptListAll);
                    sysDeptList.add(dept);
                }
            }
        }else{
            SysDept root = sysUser.getDept();
            this.getChildren(root,sysDeptList);
            sysDeptList.add(root);
        }

        return sysDeptList;
    }

    /**
     * 获取根节点的子节点
     * @param root
     * @param deptListAll
     */
    public void getChildren(SysDept root,List<SysDept> deptListAll){
        List<SysDept> childDeptList = new ArrayList<SysDept>();
        for (SysDept dept : deptListAll) {
            if(Objects.equals(root.getDeptId(), dept.getParentId())){
                childDeptList.add(dept);
                getChildren(dept,deptListAll);
            }
        }
        root.setChildren(childDeptList);
    }
}
