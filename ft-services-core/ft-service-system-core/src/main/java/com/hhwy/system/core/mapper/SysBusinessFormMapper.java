package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysBusinessForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务流程单Mapper接口
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
public interface SysBusinessFormMapper {
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
    int insertSysBusinessForm(SysBusinessForm sysBusinessForm);

    /**
     * 修改业务流程单
     * 
     * @param sysBusinessForm 业务流程单
     * @return 结果
     */
    int updateSysBusinessForm(SysBusinessForm sysBusinessForm);

    /**
     * 删除业务流程单
     * 
     * @param id 业务流程单ID
     * @return 结果
     */
    int deleteSysBusinessFormById(Long id);

    /**
     * 批量删除业务流程单
     * 
     * @param flowFormId 需要删除的数据ID
     * @return 结果
     */
    int deleteSysBusinessFormByIds(String[] flowFormId);

    /**
     * 根据功能模块查询数据库表
     * @param table
     * @return
     */
    List<SysBusinessForm> selectOmsSysFlowGetTable(String table);

    /**
     * pc流程列表展示
     * @param module
     * @param formCode
     * @return
     */
    List<SysBusinessForm> selectConfiggurationList(@Param("module") String module, @Param("formCode") String formCode, @Param("tableName") String tableName);

    /**
     * 根据编码查询信息
     * @param formCode
     * @return
     */
    SysBusinessForm selectActCodeByactCode(@Param("formCode") String formCode);

    /**
     * 根据flowFormId查询详情信息
     * @param flowFormId
     * @return
     */
    SysBusinessForm selectOmsSysFlowByFlowForData(@Param("flowFormId") String flowFormId);

}
