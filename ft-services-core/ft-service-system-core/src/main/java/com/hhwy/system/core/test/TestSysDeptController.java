package com.hhwy.system.core.test;

import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.system.api.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 部门信息
 *
 * @author hhwy
 */
//@RestController
//@RequestMapping("/dept")
public class TestSysDeptController extends BaseController {

    @Autowired
    private TestSysDeptControllerAdapter testSysDeptControllerAdapter;

    /**
     * 获取部门列表
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        return testSysDeptControllerAdapter.list(dept);
    }

    /**
     * 获取部门树形列表
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/treeList")
    public AjaxResult treeList(SysDept dept) {
        return testSysDeptControllerAdapter.treeList(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        return testSysDeptControllerAdapter.excludeChild(deptId);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return testSysDeptControllerAdapter.getInfo(deptId);
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @PreAuthorize(hasPermi = "system:dept:query")
    public AjaxResult treeSelect(SysDept dept) {
        return testSysDeptControllerAdapter.treeSelect(dept);
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @PreAuthorize(hasPermi = "system:dept:query")
    public AjaxResult roleDeptTreeSelect(@PathVariable("roleId") Long roleId) {
        return testSysDeptControllerAdapter.roleDeptTreeSelect(roleId);
    }

    /**
     * 新增部门
     */
    @PreAuthorize(hasPermi = "system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        return testSysDeptControllerAdapter.add(dept);
    }

    /**
     * 修改部门
     */
    @PreAuthorize(hasPermi = "system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        return testSysDeptControllerAdapter.edit(dept);
    }

    /**
     * 删除部门
     */
    @PreAuthorize(hasPermi = "system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        return testSysDeptControllerAdapter.remove(deptId);
    }
}
