package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.po.ActiTaskAgent;
import com.hhwy.activiti.core.service.IActiTaskAgentService;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理Controller
 * 
 * @author jzq
 * @date 2021-08-31
 */
@RestController
@RequestMapping("/act/agent")
public class ActiTaskAgentController extends BaseController {

    @Autowired
    private IActiTaskAgentService actiTaskAgentService;

    /**
     * 查询代理列表
     */
    @GetMapping("/list")
    public AjaxResult list(ActiTaskAgent actiTaskAgent) {
        startPage();
        List<ActiTaskAgent> list = actiTaskAgentService.selectActiTaskAgentList(actiTaskAgent);
        return getDataTableAjaxResult(list);
    }

    /**
     * 查询代理详情
     */
    @GetMapping("/{id}")
    public AjaxResult list(@PathVariable Long id) {
        ActiTaskAgent actiTaskAgent = actiTaskAgentService.selectActiTaskAgentById(id);
        return AjaxResult.success(actiTaskAgent);
    }

    /**
     * 新增保存代理
     */
    @Log(title = "代理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(ActiTaskAgent actiTaskAgent) {
        return toAjax(actiTaskAgentService.insertActiTaskAgent(actiTaskAgent));
    }

    /**
     * 修改保存代理
     */
    @Log(title = "代理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult editSave(ActiTaskAgent actiTaskAgent) {
        return toAjax(actiTaskAgentService.updateActiTaskAgent(actiTaskAgent));
    }

    /**
     * 删除代理
     */
    @Log(title = "代理", businessType = BusinessType.DELETE)
    @DeleteMapping( "/{ids}")
    public AjaxResult remove(@PathVariable String ids) {
        return toAjax(actiTaskAgentService.deleteActiTaskAgentByIds(ids));
    }
}
