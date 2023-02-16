package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.activiti.core.service.IActiBusinessInfoService;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 流程-业务Controller
 * 
 * @author jzq
 * @date 2021-08-24
 */
@RestController
@RequestMapping("/act/business")
public class ActiBusinessInfoController extends BaseController {

    @Autowired
    private IActiBusinessInfoService actiBusinessInfoService;

    /**
     * 查询流程-业务列表
     */
    @GetMapping("/list")
    public AjaxResult list(ActiBusinessInfo actiBusinessInfo) {
        startPage();
        List<ActiBusinessInfo> list = actiBusinessInfoService.selectActiBusinessInfoList(actiBusinessInfo);
        return getDataTableAjaxResult(list);
    }

    /**
     * 根据表名称和业务id获取详细信息
     */
    @GetMapping(value = "/{tenantKey}/{tableName}/{businessId}")
    public R<ActiBusinessInfo> getInfo(@PathVariable String tenantKey, @PathVariable String tableName, @PathVariable String businessId) {
        return R.ok(actiBusinessInfoService.selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(tableName,businessId,tenantKey));
    }


    /**
     * 新增保存流程-业务
     */
    @Log(title = "流程-业务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(ActiBusinessInfo actiBusinessInfo) {
        return toAjax(actiBusinessInfoService.insertActiBusinessInfo(actiBusinessInfo));
    }

    /**
     * 修改保存流程-业务
     */
    @Log(title = "流程-业务", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult editSave(ActiBusinessInfo actiBusinessInfo) {
        return toAjax(actiBusinessInfoService.updateActiBusinessInfo(actiBusinessInfo));
    }

    /**
     * 删除流程-业务
     */
    @Log(title = "流程-业务", businessType = BusinessType.DELETE)
    @DeleteMapping( "/remove")
    public AjaxResult remove(String ids) {
        return toAjax(actiBusinessInfoService.deleteActiBusinessInfoByIds(ids));
    }
}
