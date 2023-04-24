package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysBusinessFormItem;
import com.hhwy.system.core.domain.vo.TableAll;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务流程单子Mapper接口
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
public interface SysBusinessFormItemMapper {
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
     * 删除业务流程单子
     * 
     * @param id 业务流程单子ID
     * @return 结果
     */
    int deleteSysBusinessFormItemById(Long id);

    /**
     * 批量删除业务流程单子
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
    int deleteSysBusinessFormItemByIds(@Param("id") String[] id);

    /**
     * 查询
     * @return
     */
    List<SysBusinessFormItem> selectSysBusinessFormItemListEr(@Param("flowFormId") String flowFormId);

    /**
     * 根据业务id查询信息
     * @param flowFormId
     * @return
     */
    List<SysBusinessFormItem> selectOmsListId(@Param("flowFormId") String flowFormId);

    /**
     * 根据模块名和模块编码查询数据库
     * @param tableDbCode
     * @param table
     * @return
     */
    List<TableAll> selectOmsSysTable(@Param("tableDbCode") String tableDbCode, @Param("table") String table);


    void updateSysBusinessFormItems(@Param("sysBusinessFormItem") SysBusinessFormItem sysBusinessFormItem);

    List<SysBusinessFormItem> selectById(@Param("toStrArray") String[] toStrArray);

    List<SysBusinessFormItem> selectListId(@Param("flowFormId") String flowFormId);

    List<SysBusinessFormItem> selectOmsListEry(@Param("flowFormId") String flowFormId);

//    List<SysBusinessFormItemResult> selectDbWhereParamListByColName(@Param("colName") String colName);

    int checkDbWhereParam(@Param("colName") String colName, @Param("dbWhereParam") String dbWhereParam);
}
