package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.service.IActReModelService;
import com.hhwy.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "流程模型")
@RestController
@RequestMapping("/act/reModel")
public class ActReModelController {

    @Autowired
    private IActReModelService actReModelService;

    /**
     * 查询所有已配置的流程图
     * @return
     */
    @ApiOperation("查询所有已配置的流程图")
    @GetMapping("/getAllModel")
    public AjaxResult getAllModel(){
        return AjaxResult.success(actReModelService.getAllModel());
    }
}
