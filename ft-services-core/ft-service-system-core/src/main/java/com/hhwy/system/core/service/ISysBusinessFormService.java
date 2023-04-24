package com.hhwy.system.core.service;

import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.core.domain.SysBusinessForm;

import java.util.List;

/**
 * 业务流程单Service接口
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
public interface ISysBusinessFormService {
    /**
     * 查询业务流程单
     * 
     * @param id 业务流程单ID
     * @return 业务流程单
     */
    SysBusinessForm selectSysBusinessFormById(Long id);

    /**
     * 查询业务流程单列表
     * 
     * @param sysBusinessForm 业务流程单
     * @return 业务流程单集合
     */
    List<SysBusinessForm> selectSysBusinessFormList(SysBusinessForm sysBusinessForm);

    /**
     * 新增业务流程单
     * 
     * @param sysBusinessForm 业务流程单
     * @return 结果
     */
    AjaxResult insertSysBusinessForm(SysBusinessForm sysBusinessForm);

    /**
     * 修改业务流程单
     * 
     * @param sysBusinessForm 业务流程单
     * @return 结果
     */
    int updateSysBusinessForm(SysBusinessForm sysBusinessForm);

    /**
     * 修改业务流程单
     *
     * @param sysBusinessForm 业务流程单代码测试
     * @return 结果
     */
    AjaxResult updateSysBusinessFormEr(SysBusinessForm sysBusinessForm);

    /**
     * 批量删除业务流程单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysBusinessFormByIds(String ids);

    /**
     * 删除业务流程单信息
     * 
     * @param id 业务流程单ID
     * @return 结果
     */
    int deleteSysBusinessFormById(Long id);

    /**
     * 根据功能模块查询数据库表
     * @param module
     * @return
     */
    List<SysBusinessForm> selectDataBaseGetTable(String module);

    /**
     * 查询pc端业务流程表单配置化列表
     * @return
     */
    List<SysBusinessForm> selectConfigurationList(String module,String formCode,String tableName);

    /**
     * 根据编码查询对应的id
     * @param formCode
     * @return
     */
//    SysBusinessForm selectActCode(String formCode);
}
