package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.service.IActFormHistoryDataService;
import com.hhwy.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:03
 * <br>备注：无
 */
@RestController
@RequestMapping("/act/fromData")
public class ActiFormDataController {
    @Autowired
    private IActFormHistoryDataService formHistoryDataService;

    @GetMapping(value = "/{instanceId}")
    public AjaxResult fromData(@PathVariable("instanceId") String instanceId) {
        return AjaxResult.success(formHistoryDataService.historyDataShow(instanceId));
    }
}
