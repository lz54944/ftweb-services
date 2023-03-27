package com.hhwy.system.core.register.service.impl;

import com.hhwy.system.core.register.domain.SysRegister;
import com.hhwy.system.core.register.mapper.SysRegisterMapper;
import com.hhwy.system.core.register.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author : zhangguozhi
 * @date : 2023/3/27 10:50
 */
@Service
public class SysRegisterServiceImpl implements SysRegisterService {

    @Autowired
    private SysRegisterMapper sysRegisterMapper;
    @Override
    public List<SysRegister> selectAll() {
        return sysRegisterMapper.selectAll();
    }

    @Override
    public int insertSysRegister(SysRegister sysRegister) {
        sysRegister.setCreateTime(new Date());
        return sysRegisterMapper.insertSysRegister(sysRegister);
    }

}
