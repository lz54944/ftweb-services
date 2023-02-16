package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.po.ActiTaskAgent;
import com.hhwy.activiti.core.mapper.ActiTaskAgentMapper;
import com.hhwy.activiti.core.service.IActiTaskAgentService;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理Service业务层处理
 * 
 * @author jzq
 * @date 2021-08-31
 */
@Service
public class ActiTaskAgentServiceImpl implements IActiTaskAgentService {

    @Autowired
    private ActiTaskAgentMapper actiTaskAgentMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询代理
     * 
     * @param id 代理ID
     * @return 代理
     */
    @Override
    public ActiTaskAgent selectActiTaskAgentById(Long id) {
        return actiTaskAgentMapper.selectActiTaskAgentById(id);
    }

    /**
     * 查询代理列表
     * 
     * @param actiTaskAgent 代理
     * @return 代理
     */
    @Override
    public List<ActiTaskAgent> selectActiTaskAgentList(ActiTaskAgent actiTaskAgent) {
        actiTaskAgent.setTenantKey(tokenService.getTenantKey());
        return actiTaskAgentMapper.selectActiTaskAgentList(actiTaskAgent);
    }

    /**
     * 新增代理
     * 
     * @param actiTaskAgent 代理
     * @return 结果
     */
    @Override
    public int insertActiTaskAgent(ActiTaskAgent actiTaskAgent) {
        actiTaskAgent.setCreateTime(DateUtils.getNowDate());
        actiTaskAgent.setTenantKey(tokenService.getTenantKey());
        return actiTaskAgentMapper.insertActiTaskAgent(actiTaskAgent);
    }

    /**
     * 修改代理
     * 
     * @param actiTaskAgent 代理
     * @return 结果
     */
    @Override
    public int updateActiTaskAgent(ActiTaskAgent actiTaskAgent) {
        actiTaskAgent.setUpdateTime(DateUtils.getNowDate());
        return actiTaskAgentMapper.updateActiTaskAgent(actiTaskAgent);
    }

    /**
     * 删除代理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActiTaskAgentByIds(String ids) {
        return actiTaskAgentMapper.deleteActiTaskAgentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除代理信息
     * 
     * @param id 代理ID
     * @return 结果
     */
    @Override
    public int deleteActiTaskAgentById(Long id) {
        return actiTaskAgentMapper.deleteActiTaskAgentById(id);
    }
}
