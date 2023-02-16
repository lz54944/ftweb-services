package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.dto.ActivitiHighLineDTO;
import com.hhwy.activiti.core.service.IActHistoryService;
import com.hhwy.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:03
 * <br>备注：无
 */
@RestController
@RequestMapping("/act/history")
public class ActHistoryController {

    @Autowired
    private IActHistoryService activitiHistoryService;

    //流程图高亮
    @GetMapping("/gethighLine")
    public AjaxResult gethighLine(@RequestParam("instanceId") String instanceId) {
        ActivitiHighLineDTO activitiHighLineDTO = activitiHistoryService.gethighLine(instanceId);
        return AjaxResult.success(activitiHighLineDTO);
    }

}
