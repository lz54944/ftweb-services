package com.hhwy.system.core.controller;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.core.service.ISysTenantService;
import com.hhwy.system.core.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 租户管理Controller
 * 
 * @author jzq
 * @date 2021-08-11
 */
@ResponseBody
@RequestMapping("/tenant")
public class SysTenantController extends BaseController {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询租户管理列表
     */
    @GetMapping("/list")
    @PreAuthorize(hasPermi = "system:tenant:query")
    public AjaxResult list(SysTenant sysTenant) {
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTableAjaxResult(list);
    }

    /**
     * 查询租户管理列表
     */
    @GetMapping("/list/all")
    @PreAuthorize(hasPermi = "system:tenant:query")
    public AjaxResult listAll(SysTenant sysTenant) {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return AjaxResult.success(list);
    }

    /**
     * 查询租户管理列表
     */
    @GetMapping("/getAllTenant")
    public AjaxResult getAllTenant() {
        SysTenant sysTenant = new SysTenant();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return AjaxResult.success(list);
    }

    @GetMapping("/getTenantByTenantKey")
    public AjaxResult getTenantByTenantKey(@RequestParam("tenantKey") String tenantKey) {
        return AjaxResult.success(sysTenantService.selectSysTenantByTenantKey(tenantKey));
    }

    /**
     * 查询租户详情
     */
    @GetMapping("/{tenantId}")
    @PreAuthorize(hasPermi = "system:tenant:query")
    public AjaxResult detail(@PathVariable("tenantId") Long tenantId) {
        SysTenant sysTenant = sysTenantService.selectSysTenantById(tenantId);
        return AjaxResult.success(sysTenant);
    }

    /**
     * 新增保存租户管理
     */
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize(hasPermi = "system:tenant:add")
    public AjaxResult addSave(@RequestBody SysTenant sysTenant) {
        if (!Constants.MASTER_TENANT_KEY.equals(tokenService.getTenantKey())) {
            return AjaxResult.error("非"+Constants.MASTER_TENANT_NAME+"无权操作");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysTenant.getAdministratorUserName()))
                ||UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUniqueByTenantKey(sysTenant.getTenantKey(),sysTenant.getAdministratorUserName()))) {
            return AjaxResult.error("管理员'" + sysTenant.getAdministratorUserName() + "'添加失败，账号已存在");
        }
        return toAjax(sysTenantService.insertSysTenant(sysTenant));
    }

    /**
     * 修改保存租户管理
     */
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize(hasPermi = "system:tenant:edit")
    public AjaxResult editSave(@RequestBody SysTenant sysTenant) {
        if (!Constants.MASTER_TENANT_KEY.equals(tokenService.getTenantKey())) {
            return AjaxResult.error("非"+Constants.MASTER_TENANT_NAME+"无权操作");
        }
        return toAjax(sysTenantService.updateSysTenant(sysTenant));
    }

    /**
     * 删除租户管理
     */
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    @DeleteMapping( "/{ids}")
    @PreAuthorize(hasPermi = "system:tenant:remove")
    public AjaxResult remove(@PathVariable String ids) {
        if (!Constants.MASTER_TENANT_KEY.equals(tokenService.getTenantKey())) {
            return AjaxResult.error("非"+Constants.MASTER_TENANT_NAME+"无权操作");
        }
        return toAjax(sysTenantService.deleteSysTenantByIds(ids));
    }
}
