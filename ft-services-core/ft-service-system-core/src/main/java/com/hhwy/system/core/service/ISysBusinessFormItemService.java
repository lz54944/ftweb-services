package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysBusinessFormItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务流程单子Service接口
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
public interface ISysBusinessFormItemService {
    /**
     * 查询业务流程单子
     * 
     * @param id 业务流程单子ID
     * @return 业务流程单子
     */
    SysBusinessFormItem selectSysBusinessFormItemById(Long id);

    /**
     * 查询业务流程单子列表
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 业务流程单子集合
     */
    List<SysBusinessFormItem> selectSysBusinessFormItemList(SysBusinessFormItem sysBusinessFormItem);

    /**
     * 新增业务流程单子
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 结果
     */
    int insertSysBusinessFormItem(SysBusinessFormItem sysBusinessFormItem);

    /**
     * 修改业务流程单子
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 结果
     */
    int updateSysBusinessFormItem(SysBusinessFormItem sysBusinessFormItem);

    /**
     * 批量删除业务流程单子
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
    int deleteSysBusinessFormItemByIds(String id);

    /**
     * 删除业务流程单子信息
     * 
     * @param id 业务流程单子ID
     * @return 结果
     */
    int deleteSysBusinessFormItemById(Long id);

    /**
     * 根据业务id查询信息
     * @param flowFormId
     * @return
     */
    List<SysBusinessFormItem> selectOmsList(String flowFormId);

    /**
     * pc业务流程表单生成数据子表
     * @param flowFormId
     * @param tableDbCode
     * @param module
     * @return
     */
    List<SysBusinessFormItem> selectOmsSysFlowGenerateData(String flowFormId, String tableDbCode, String module);

    List<SysBusinessFormItem> selectSysBusinessFormItemByIdList(String id);

    List<SysBusinessFormItem> selectOmsLister(String flowFormId);

    List<SysBusinessFormItem> selectOmsListery(@Param("flowFormId") String flowFormId);

//    List<SysBusinessFormItemResult> queryDbWhereParamListByColName(@Param("colName") String colName);
}
