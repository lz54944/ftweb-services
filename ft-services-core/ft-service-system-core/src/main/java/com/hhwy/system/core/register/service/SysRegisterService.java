package com.hhwy.system.core.register.service;


import com.hhwy.system.core.register.domain.SysRegister;

import java.util.List;

public interface SysRegisterService {

    List<SysRegister> selectAll();

//    int insertSysRegisters(List<SysRegister> sysRegisters);

    int insertSysRegister(SysRegister sysRegisters);

}
