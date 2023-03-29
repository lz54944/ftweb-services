package com.hhwy.system.core.controller;

import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.TableDataInfo;
import com.hhwy.system.core.domain.SysNoticeRecipient;
import com.hhwy.system.core.service.ISysNoticeRecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 消息接收人Controller
 * 
 * @author 韩
 * @date 2022-12-23
 */
@RestController
@RequestMapping("/inform/recipient")
public class SysNoticeRecipientController extends BaseController {
    @Autowired
    private ISysNoticeRecipientService sysNoticeRecipientService;

    /**
     * 查询消息接收人列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysNoticeRecipient sysNoticeRecipient) {
        startPage();
        List<SysNoticeRecipient> list = sysNoticeRecipientService.selectSysNoticeRecipientList(sysNoticeRecipient);
        return getDataTable(list);
    }

    /**
     * 获取消息接收人详细信息
     */
    @GetMapping(value = "get/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysNoticeRecipientService.selectSysNoticeRecipientById(id));
    }

    /**
     * 新增消息接收人
     */
    @PostMapping("add")
    public AjaxResult add(@RequestBody SysNoticeRecipient sysNoticeRecipient) {
        return toAjax(sysNoticeRecipientService.insertSysNoticeRecipient(sysNoticeRecipient));
    }

    /**
     * 修改消息接收人
     */
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody SysNoticeRecipient sysNoticeRecipient) {
        return toAjax(sysNoticeRecipientService.updateSysNoticeRecipient(sysNoticeRecipient));
    }

    /**
     * 删除消息接收人
     */
	@DeleteMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysNoticeRecipientService.deleteSysNoticeRecipientByIds(ids));
    }
}
