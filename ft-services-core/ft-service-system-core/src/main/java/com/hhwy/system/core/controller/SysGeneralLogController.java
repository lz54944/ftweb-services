package com.hhwy.system.core.controller;

import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.api.domain.SysGeneralLog;
import com.hhwy.system.core.service.ISysGeneralLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 通用日志记录
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/generalLog")
public class SysGeneralLogController extends BaseController {
    @Autowired
    private ISysGeneralLogService sysGeneralLogService;

    /**
     * 查询通用日志列表
     */
    @GetMapping("/list")

    public AjaxResult list(SysGeneralLog sysGeneralLog) {
        startPage();
        List<SysGeneralLog> list = sysGeneralLogService.selectSysGeneralLogList(sysGeneralLog);
        return getDataTableAjaxResult(list);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{generalLogId}")
    public AjaxResult detail(@PathVariable Long generalLogId) {
        SysGeneralLog sysGeneralLog = sysGeneralLogService.selectSysGeneralLogById(generalLogId);
        return AjaxResult.success(sysGeneralLog);
    }

    /**
     * 新增保存通用日志
     */
    @PostMapping
    public AjaxResult addSave(@RequestBody SysGeneralLog sysGeneralLog) {
        return toAjax(sysGeneralLogService.insertSysGeneralLog(sysGeneralLog));
    }

    /**
     * 删除通用日志
     */
    @DeleteMapping("/{generalLogIds}")
    public AjaxResult remove(@PathVariable String generalLogIds) {
        return toAjax(sysGeneralLogService.deleteSysGeneralLogByIds(generalLogIds));
    }

}
