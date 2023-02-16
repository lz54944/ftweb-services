package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import com.hhwy.activiti.core.mapper.ActiTaskConfigMapper;
import com.hhwy.activiti.core.service.IActiTaskConfigService;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <br>描 述： ActiTaskConfigServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActiTaskConfigServiceImpl implements IActiTaskConfigService {

    @Autowired
    private ActiTaskConfigMapper actiTaskconfigMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ActiTaskConfig selectActiTaskConfigById(Long id) {
        return actiTaskconfigMapper.selectActiTaskConfigById(id);
    }

    @Override
    public ActiTaskConfig selectLatestActiTaskConfigByProcDefKeyAndTaskNodeId(String procDefKey, String taskNodeId, String tenantKey) {
        return actiTaskconfigMapper.selectLatestActiTaskConfigByProcDefKeyAndTaskNodeId(procDefKey,taskNodeId,tenantKey);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ActiTaskConfig> selectActiTaskConfigList(ActiTaskConfig actiTaskconfig) {
        return actiTaskconfigMapper.selectActiTaskConfigList(actiTaskconfig,tokenService.getTenantKey());
    }

    @Override
    public List<ActiTaskConfig> selectActiTaskConfigListByProcDefId(String procDefId, String tenantKey) {
        ActiTaskConfig actiTaskconfig = new ActiTaskConfig();
        actiTaskconfig.setProcDefId(procDefId);
        return actiTaskconfigMapper.selectActiTaskConfigList(actiTaskconfig,tenantKey);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertActiTaskConfig(ActiTaskConfig actiTaskconfig) {
        actiTaskconfig.setCreateTime(DateUtils.getNowDate());
        actiTaskconfig.setTenantKey(tokenService.getTenantKey());
        return actiTaskconfigMapper.insertActiTaskConfig(actiTaskconfig);
    }

    @Override
    public int batchInsertActiTaskConfig(List<ActiTaskConfig> actiTaskConfigList) {
        return actiTaskconfigMapper.batchInsertActiTaskConfig(actiTaskConfigList,tokenService.getTenantKey());
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param actiTaskconfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateActiTaskConfig(ActiTaskConfig actiTaskconfig) {
        actiTaskconfig.setUpdateTime(DateUtils.getNowDate());
        return actiTaskconfigMapper.updateActiTaskConfig(actiTaskconfig);
    }

    @Override
    @Transactional
    public int editSave(List<ActiTaskConfig> actiTaskConfigList) {
        ActiTaskConfig actiTaskConfig = actiTaskConfigList.get(0);
        String procDefId = actiTaskConfig.getProcDefId();
        Assert.notNull(procDefId, "procDefId不能为空！");
        actiTaskconfigMapper.deleteActiTaskConfigByProcDefId(procDefId,tokenService.getTenantKey());
        return actiTaskconfigMapper.batchInsertActiTaskConfig(actiTaskConfigList,tokenService.getTenantKey());
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActiTaskConfigByIds(String ids) {
        return actiTaskconfigMapper.deleteActiTaskConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteActiTaskConfigById(Long id) {
        return actiTaskconfigMapper.deleteActiTaskConfigById(id);
    }
}
