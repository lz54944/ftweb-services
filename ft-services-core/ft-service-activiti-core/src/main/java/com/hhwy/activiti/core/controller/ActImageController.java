package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.service.impl.ActImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>描 述： ActImageController
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/10 15:01
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@RestController
@RequestMapping("/act/img")
public class ActImageController {
    @Autowired
    ActImageServiceImpl actImageServiceImpl;

     /**
        * <br>方法描述：获取动态流程图
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/8/10 10:34
        * <br>备注：无
        */
    @GetMapping("/dynamicSvg/{instanceId}")
    public String test(@PathVariable String instanceId) throws Exception{
        return actImageServiceImpl.getFlowImgByProcInstId(instanceId);
    }
}
