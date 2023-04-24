package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.system.core.domain.SysBusinessFormItem;
import com.hhwy.system.core.domain.vo.TableAll;
import com.hhwy.system.core.mapper.SysBusinessFormItemMapper;
import com.hhwy.system.core.service.ISysBusinessFormItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务流程单子Service业务层处理
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Service
public class SysBusinessFormItemServiceImpl implements ISysBusinessFormItemService {
    @Autowired
    private SysBusinessFormItemMapper sysBusinessFormItemMapper;

//    @Autowired
//    private DictTableColumnMapper dictTableColumnMapper;

    /**
     * 查询业务流程单子
     * 
     * @param id 业务流程单子ID
     * @return 业务流程单子
     */
    @Override
    public SysBusinessFormItem selectSysBusinessFormItemById(Long id) {
        return sysBusinessFormItemMapper.selectSysBusinessFormItemById(id);
    }

    /**
     * 查询业务流程单子列表
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 业务流程单子
     */
    @Override
    public List<SysBusinessFormItem> selectSysBusinessFormItemList(SysBusinessFormItem sysBusinessFormItem) {
        return sysBusinessFormItemMapper.selectSysBusinessFormItemList(sysBusinessFormItem);
    }

    /**
     * 新增业务流程单子
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 结果
     */
    @Override
    public int insertSysBusinessFormItem(SysBusinessFormItem sysBusinessFormItem) {
//        AddBaseInfoUtil baseEntityAddBaseInfoUtil = new AddBaseInfoUtil<>();
//        baseEntityAddBaseInfoUtil.add(sysBusinessFormItem);
        return sysBusinessFormItemMapper.insertSysBusinessFormItem(sysBusinessFormItem);
    }

    /**
     * 修改业务流程单子
     * 
     * @param sysBusinessFormItem 业务流程单子
     * @return 结果
     */
    @Override
    public int updateSysBusinessFormItem(SysBusinessFormItem sysBusinessFormItem) {
        sysBusinessFormItem.setUpdateTime(DateUtils.getNowDate());
        return sysBusinessFormItemMapper.updateSysBusinessFormItem(sysBusinessFormItem);
    }

    /**
     * 删除业务流程单子对象
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysBusinessFormItemByIds(String id) {

        return sysBusinessFormItemMapper.deleteSysBusinessFormItemByIds(Convert.toStrArray(id));
    }

    /**
     * 删除业务流程单子信息
     * 
     * @param id 业务流程单子ID
     * @return 结果
     */
    @Override
    public int deleteSysBusinessFormItemById(Long id) {
        return sysBusinessFormItemMapper.deleteSysBusinessFormItemById(id);
    }

    /**
     * 根据业务id查询子表信息
     * @param flowFormId
     * @return
     */
    @Override
    public List<SysBusinessFormItem> selectOmsList(String flowFormId) {
        return sysBusinessFormItemMapper.selectOmsListId(flowFormId);

    }

    /**
     * 生成数据事件
     * @param flowFormId
     * @param tableDbCode
     * @param module
     * @return
     */
    @Override
    public List<SysBusinessFormItem> selectOmsSysFlowGenerateData(String flowFormId, String tableDbCode, String module) {
      //先查询子表数据有没有值

        //查询主表数据库
        List<TableAll> tableAll = sysBusinessFormItemMapper.selectOmsSysTable(tableDbCode, module);
        tableAll.forEach(l->{
            SysBusinessFormItem sysBusinessFormItem = new SysBusinessFormItem();
            //将查出的数据复制到子表中
//                AddBaseInfoUtil baseEntityAddBaseInfoUtil = new AddBaseInfoUtil<>();
//                baseEntityAddBaseInfoUtil.add(sysBusinessFormItem);
            sysBusinessFormItem.setFlowFormId(flowFormId);
            sysBusinessFormItem.setColCode(l.getColumnName());
            sysBusinessFormItem.setColComm(l.getColumnComment());
            sysBusinessFormItem.setColType(l.getDataType());
            sysBusinessFormItem.setOrderno(l.getOrdinalPosition());
            sysBusinessFormItem.setColName(l.getTableName());

            //获取字典编码
//                String tableName = l.getTableName();
//                GenTable genTable = dictTableColumnMapper.selectTableNameGetTableId(tableName);
//                String tableId = genTable.getTableId().toString();
//                List<GenTableColumn> genTableColumnList = dictTableColumnMapper.selectTableByIdGetTable(tableId);
//                genTableColumnList.forEach(o->{
//                    if (o.getDictType() != null && o.getColumnName().equals(sysBusinessFormItem.getColCode())) {
//                        String dictType = o.getDictType();
//                        sysBusinessFormItem.setDictCode(dictType);
//                    }
//                });
            sysBusinessFormItemMapper.insertSysBusinessFormItem(sysBusinessFormItem);
        });
        //根据业务id返回插入的值
        List<SysBusinessFormItem> sysBusinessFormItemEry = sysBusinessFormItemMapper.selectOmsListId(flowFormId);
        return sysBusinessFormItemEry;
    }

    /**
     * 根据批量id查询数据
     * @param id
     * @return
     */
    @Override
    public List<SysBusinessFormItem> selectSysBusinessFormItemByIdList(String id) {
        return sysBusinessFormItemMapper.selectById(Convert.toStrArray(id));
    }

    @Override
    public List<SysBusinessFormItem> selectOmsLister(String flowFormId) {
        return sysBusinessFormItemMapper.selectListId(flowFormId);
    }

    @Override
    public List<SysBusinessFormItem> selectOmsListery(String flowFormId) {
        return sysBusinessFormItemMapper.selectOmsListEry(flowFormId);
    }

//    @Override
//    public List<SysBusinessFormItemResult> queryDbWhereParamListByColName(String colName) {
//        return sysBusinessFormItemMapper.selectDbWhereParamListByColName(colName);
//    }
}
