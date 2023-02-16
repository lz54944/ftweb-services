package com.hhwy.system.core.service.impl;

import java.util.List;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.system.api.domain.SysGeneralLog;
import com.hhwy.system.core.mapper.SysGeneralLogMapper;
import com.hhwy.system.core.service.ISysGeneralLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hhwy.common.core.text.Convert;

/**
 * 通用日志Service业务层处理
 * 
 * @author jzq
 * @date 2022-02-18
 */
@Service
public class SysGeneralLogServiceImpl implements ISysGeneralLogService {
    @Autowired
    private SysGeneralLogMapper sysGeneralLogMapper;

    /**
     * 查询通用日志
     * 
     * @param logId 通用日志ID
     * @return 通用日志
     */
    @Override
    public SysGeneralLog selectSysGeneralLogById(Long logId) {
        return sysGeneralLogMapper.selectSysGeneralLogById(logId);
    }

    /**
     * 查询通用日志列表
     * 
     * @param sysGeneralLog 通用日志
     * @return 通用日志
     */
    @Override
    public List<SysGeneralLog> selectSysGeneralLogList(SysGeneralLog sysGeneralLog) {
        return sysGeneralLogMapper.selectSysGeneralLogList(sysGeneralLog);
    }

    /**
     * 新增通用日志
     * 
     * @param sysGeneralLog 通用日志
     * @return 结果
     */
    @Override
    public int insertSysGeneralLog(SysGeneralLog sysGeneralLog) {
        sysGeneralLog.setCreateTime(DateUtils.getNowDate());
        return sysGeneralLogMapper.insertSysGeneralLog(sysGeneralLog);
    }

    /**
     * 修改通用日志
     * 
     * @param sysGeneralLog 通用日志
     * @return 结果
     */
    @Override
    public int updateSysGeneralLog(SysGeneralLog sysGeneralLog) {
        sysGeneralLog.setUpdateTime(DateUtils.getNowDate());
        return sysGeneralLogMapper.updateSysGeneralLog(sysGeneralLog);
    }

    /**
     * 删除通用日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysGeneralLogByIds(String ids) {
        return sysGeneralLogMapper.deleteSysGeneralLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通用日志信息
     * 
     * @param logId 通用日志ID
     * @return 结果
     */
    @Override
    public int deleteSysGeneralLogById(Long logId) {
        return sysGeneralLogMapper.deleteSysGeneralLogById(logId);
    }
}
