package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.po.ActiTodoTask;
import com.hhwy.activiti.core.service.IActiTodoTaskService;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 待办Controller
 *
 * @author jzq
 * @date 2021-07-20
 */
@RestController
@RequestMapping("/act/todoTask")
public class ActiTodoTaskController extends BaseController {

    @Autowired
    private IActiTodoTaskService actiTodoTaskService;

    /**
     * 查询待办列表
     */
    @GetMapping("/list")
    public AjaxResult list(ActiTodoTask actiTodoTask) {
        startPage();
        List<ActiTodoTask> list = actiTodoTaskService.selectActiTodoTaskList(actiTodoTask);
        return getDataTableAjaxResult(list);
    }

    /**
     * 新增保存待办
     */
    /*@Log(title = "待办", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(ActiTodoTask actiTodoTask) {
        return toAjax(actiTodoTaskService.insertActiTodoTask(actiTodoTask));
    }*/

    /**
     * 修改保存待办
     */
    /*@Log(title = "待办", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult editSave(ActiTodoTask actiTodoTask) {
        return toAjax(actiTodoTaskService.updateActiTodoTask(actiTodoTask));
    }*/

    /**
     * 删除待办
     */
    @Log(title = "待办", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public AjaxResult remove(String ids) {
        return toAjax(actiTodoTaskService.deleteActiTodoTaskByIds(ids));
    }
}
