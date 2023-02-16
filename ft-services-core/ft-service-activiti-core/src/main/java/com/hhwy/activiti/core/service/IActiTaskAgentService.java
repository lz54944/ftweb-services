package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.po.ActiTaskAgent;

import java.util.List;

/**
 * 代理Service接口
 * 
 * @author jzq
 * @date 2021-08-31
 */
public interface IActiTaskAgentService {
    /**
     * 查询代理
     * 
     * @param id 代理ID
     * @return 代理
     */
    ActiTaskAgent selectActiTaskAgentById(Long id);

    /**
     * 查询代理列表
     * 
     * @param actiTaskAgent 代理
     * @return 代理集合
     */
    List<ActiTaskAgent> selectActiTaskAgentList(ActiTaskAgent actiTaskAgent);

    /**
     * 新增代理
     * 
     * @param actiTaskAgent 代理
     * @return 结果
     */
    int insertActiTaskAgent(ActiTaskAgent actiTaskAgent);

    /**
     * 修改代理
     * 
     * @param actiTaskAgent 代理
     * @return 结果
     */
    int updateActiTaskAgent(ActiTaskAgent actiTaskAgent);

    /**
     * 批量删除代理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTaskAgentByIds(String ids);

    /**
     * 删除代理信息
     * 
     * @param id 代理ID
     * @return 结果
     */
    int deleteActiTaskAgentById(Long id);
}
