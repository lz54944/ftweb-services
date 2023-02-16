package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import java.util.List;

/**
 * <br>描 述： IActiTaskConfigService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActiTaskConfigService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    ActiTaskConfig selectActiTaskConfigById(Long id);

    ActiTaskConfig selectLatestActiTaskConfigByProcDefKeyAndTaskNodeId(String procDefKey, String taskNodeId, String tenantKey);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<ActiTaskConfig> selectActiTaskConfigList(ActiTaskConfig actiTaskconfig);

    List<ActiTaskConfig> selectActiTaskConfigListByProcDefId(String procDefId,String tenantKey);

    /**
     * 新增【请填写功能名称】
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 结果
     */
    int insertActiTaskConfig(ActiTaskConfig actiTaskconfig);

    int batchInsertActiTaskConfig(List<ActiTaskConfig> actiTaskConfigList);

    /**
     * 修改【请填写功能名称】
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 结果
     */
    int updateActiTaskConfig(ActiTaskConfig actiTaskconfig);

    int editSave(List<ActiTaskConfig> actiTaskConfigList);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTaskConfigByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    int deleteActiTaskConfigById(Long id);
}
