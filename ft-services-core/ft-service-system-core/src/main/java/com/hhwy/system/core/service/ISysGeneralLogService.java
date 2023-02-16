package com.hhwy.system.core.service;

import com.hhwy.system.api.domain.SysGeneralLog;

import java.util.List;

/**
 * 通用日志Service接口
 * 
 * @author jzq
 * @date 2022-02-18
 */
public interface ISysGeneralLogService {
    /**
     * 查询通用日志
     * 
     * @param logId 通用日志ID
     * @return 通用日志
     */
    SysGeneralLog selectSysGeneralLogById(Long logId);

    /**
     * 查询通用日志列表
     * 
     * @param sysGeneralLog 通用日志
     * @return 通用日志集合
     */
    List<SysGeneralLog> selectSysGeneralLogList(SysGeneralLog sysGeneralLog);

    /**
     * 新增通用日志
     * 
     * @param sysGeneralLog 通用日志
     * @return 结果
     */
    int insertSysGeneralLog(SysGeneralLog sysGeneralLog);

    /**
     * 修改通用日志
     * 
     * @param sysGeneralLog 通用日志
     * @return 结果
     */
    int updateSysGeneralLog(SysGeneralLog sysGeneralLog);

    /**
     * 批量删除通用日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysGeneralLogByIds(String ids);

    /**
     * 删除通用日志信息
     * 
     * @param logId 通用日志ID
     * @return 结果
     */
    int deleteSysGeneralLogById(Long logId);
}
