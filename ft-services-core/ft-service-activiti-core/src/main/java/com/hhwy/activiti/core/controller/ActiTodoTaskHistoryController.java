package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;
import com.hhwy.activiti.core.service.IActiTodoTaskHistoryService;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 已办任务Controller
 * 
 * @author jzq
 * @date 2021-08-03
 */
@RestController
@RequestMapping("/act/todoTaskHistory")
public class ActiTodoTaskHistoryController extends BaseController {

    @Autowired
    private IActiTodoTaskHistoryService actiTodoTaskHistoryService;

    /**
     * 查询已办任务列表
     */
    @GetMapping("/list")
    public AjaxResult list(ActiTodoTaskHistory actiTodoTaskHistory) {
        startPage();
        List<ActiTodoTaskHistory> list = actiTodoTaskHistoryService.selectActiTodoTaskHistoryList(actiTodoTaskHistory);
        return getDataTableAjaxResult(list);
    }

    /**
     * 新增保存已办任务
     */
    @Log(title = "已办任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(ActiTodoTaskHistory actiTodoTaskHistory) {
        return toAjax(actiTodoTaskHistoryService.insertActiTodoTaskHistory(actiTodoTaskHistory));
    }

    /**
     * 修改保存已办任务
     */
    @Log(title = "已办任务", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult editSave(ActiTodoTaskHistory actiTodoTaskHistory) {
        return toAjax(actiTodoTaskHistoryService.updateActiTodoTaskHistory(actiTodoTaskHistory));
    }

    /**
     * 删除已办任务
     */
    @Log(title = "已办任务", businessType = BusinessType.DELETE)
    @DeleteMapping( "/remove")
    public AjaxResult remove(String ids) {
        return toAjax(actiTodoTaskHistoryService.deleteActiTodoTaskHistoryByIds(ids));
    }
}
