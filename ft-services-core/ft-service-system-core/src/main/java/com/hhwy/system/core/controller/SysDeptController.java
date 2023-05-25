package com.hhwy.system.core.controller;

import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.system.api.domain.SysDept;
import com.hhwy.system.core.service.ISysDeptService;
import com.hhwy.system.core.utils.DeptTreeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * 获取部门树形列表
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/lazyList")
    public AjaxResult lazyList(SysDept dept) {
        List<SysDept> depts;
        if (StringUtils.isNotBlank(dept.getDeptName())||StringUtils.isNotBlank(dept.getStatus())) {
            int count = deptService.selectDeptListCount(dept);
            if(count>=200){
                return AjaxResult.error("查询结果集超过了200条，请精确查询条件");
            }
            depts = deptService.selectAllDeptList(dept);
        }else {
            if (dept.getDeptId()==null) {
                //查询一级列表
                depts = deptService.selectOneLevelDeptList(dept);
            }else {
                //查询子集
                depts = deptService.selectChildrenDeptList(dept);
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 获取部门树形列表
     */
    @Deprecated
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/treeList")
    public AjaxResult treeList(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        List<SysDept> sysTreeDeptList = new DeptTreeUtils().deptList(depts);
        return AjaxResult.success(sysTreeDeptList);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @PreAuthorize(hasPermi = "system:dept:query")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @PreAuthorize(hasPermi = "system:dept:query")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptIdListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize(hasPermi = "system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateUser(SecurityUtils.getUserName());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize(hasPermi = "system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId() != null && dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateUser(SecurityUtils.getUserName());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize(hasPermi = "system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 获取当前登录人所在部门及下级部门（嵌套列表）
     * @return
     */
    @GetMapping("getLoginUserNestDept")
    public AjaxResult getLoginUserNestDept(){
        return AjaxResult.success(deptService.getLoginUserNestDept());
    }
}
