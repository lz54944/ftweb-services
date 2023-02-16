package com.hhwy.system.core.controller;

import java.util.List;

import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.TableDataInfo;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.system.api.domain.SysTenantDb;
import com.hhwy.system.core.service.ISysTenantDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 租户-数据库信息Controller
 * 
 * @author jzq
 * @date 2021-09-29
 */
@RestController
@RequestMapping("/tenant/db")
public class SysTenantDbController extends BaseController {

    @Autowired
    private ISysTenantDbService sysTenantDbService;

    /**
     * 查询租户-数据库信息列表
     */
    @GetMapping("/list")
    @PreAuthorize(hasPermi = "system:tenant:query")
    public AjaxResult list(SysTenantDb sysTenantDb) {
        startPage();
        List<SysTenantDb> list = sysTenantDbService.selectSysTenantDbList(sysTenantDb);
        return getDataTableAjaxResult(list);
    }

    @GetMapping("/getTenantDbByServiceName")
    public R<List<SysTenantDb>> getTenantDbByServiceName(String serviceName) {
        List<SysTenantDb> list = sysTenantDbService.getTenantDbByServiceName(serviceName);
        return R.ok(list);
    }

    @GetMapping("/getTenantDbByServiceNameAndTenantKey")
    public R<SysTenantDb> getTenantDbByServiceNameAndTenantKey(String serviceName, String tenantKey) {
        SysTenantDb sysTenantDb = sysTenantDbService.getTenantDbByServiceNameAndTenantKey(serviceName,tenantKey);
        return R.ok(sysTenantDb);
    }

    /**
     * 新增保存租户-数据库信息
     */
    @PostMapping
    public R addSave(@RequestBody SysTenantDb sysTenantDb) {
        return toR(sysTenantDbService.insertSysTenantDb(sysTenantDb));
    }

    /**
     * 修改保存租户-数据库信息
     */
    @PutMapping
    public AjaxResult editSave(@RequestBody SysTenantDb sysTenantDb) {
        return toAjax(sysTenantDbService.updateSysTenantDb(sysTenantDb));
    }

    /**
     * 删除租户-数据库信息
     */
    @Log(title = "租户-数据库信息", businessType = BusinessType.DELETE)
    @DeleteMapping( "/remove")
    @PreAuthorize(hasPermi = "system:tenant:remove")
    public AjaxResult remove(String ids) {
        return toAjax(sysTenantDbService.deleteSysTenantDbByIds(ids));
    }
}
