package com.hhwy.system.core.register.mapper;

import com.hhwy.system.core.register.domain.SysRegister;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRegisterMapper {

    List<SysRegister> selectAll();

    int insertSysRegister(SysRegister sysRegister);


}
