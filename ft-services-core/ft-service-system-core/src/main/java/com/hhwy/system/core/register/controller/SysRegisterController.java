package com.hhwy.system.core.register.controller;

import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.core.register.domain.SysRegister;
import com.hhwy.system.core.register.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class SysRegisterController {

    @Autowired
    private SysRegisterService sysRegisterService;

    @GetMapping("/selectAll")
    public AjaxResult selectAll() {
        List<SysRegister> sysRegisters = sysRegisterService.selectAll();
        return AjaxResult.success("查询成功!", sysRegisters);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysRegister sysRegister) {
        int i = sysRegisterService.insertSysRegister(sysRegister);
        return AjaxResult.success("保存成功");
    }

}
