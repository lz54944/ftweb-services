package com.hhwy.demo.controller;

import java.util.List;

import com.hhwy.activiti.api.RemoteActivitiService;
import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.demo.domain.BpmDemo;
import com.hhwy.demo.service.IBpmDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流程测试Controller
 *
 * @author jzq
 * @date 2021-10-18
 */
@RestController
@RequestMapping("/bpmDemo")
public class BpmDemoController extends BaseController {

    @Autowired
    private IBpmDemoService bpmDemoService;

    @Autowired
    private RemoteActivitiService remoteActivitiService;

    @Autowired
    private TokenService tokenService;

    private final static String TABLE_NAME = "t_bpm_demo";
    /**
     * 查询流程测试列表
     */
    @GetMapping("/list")
    public AjaxResult list(BpmDemo bpmDemo) {
        startPage();
        List<BpmDemo> list = bpmDemoService.selectBpmDemoList(bpmDemo);
        for (BpmDemo demo : list) {
            ActiBusinessInfo actiBusinessInfo = remoteActivitiService.getInfo(tokenService.getTenantKey(), TABLE_NAME, demo.getId() + "").getData();
            demo.setActiBusinessInfo(actiBusinessInfo);
        }
        return getDataTableAjaxResult(list);
    }

    /**
     * 查询流程测试列表
     */
    @GetMapping("/{id}")
    public AjaxResult info(@PathVariable("id") Long id) {
        BpmDemo bpmDemo = bpmDemoService.selectBpmDemoById(id);
        ActiBusinessInfo actiBusinessInfo = remoteActivitiService.getInfo(tokenService.getTenantKey(), TABLE_NAME, bpmDemo.getId() + "").getData();
        bpmDemo.setActiBusinessInfo(actiBusinessInfo);
        return AjaxResult.success(bpmDemo);
    }

    /**
     * 新增保存流程测试
     */
    @Log(title = "流程测试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addSave(@RequestBody BpmDemo bpmDemo) {
        return toAjax(bpmDemoService.insertBpmDemo(bpmDemo));
    }

    /**
     * 修改保存流程测试
     */
    @Log(title = "流程测试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@RequestBody BpmDemo bpmDemo) {
        return toAjax(bpmDemoService.updateBpmDemo(bpmDemo));
    }

    /**
     * 删除流程测试
     */
    @Log(title = "流程测试", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String ids) {
        return toAjax(bpmDemoService.deleteBpmDemoByIds(ids));
    }

}
