package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.po.ActiTaskAgent;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 代理Mapper接口
 * 
 * @author jzq
 * @date 2021-08-31
 */
public interface ActiTaskAgentMapper {
    /**
     * 查询代理
     * 
     * @param id 代理ID
     * @return 代理
     */
    ActiTaskAgent selectActiTaskAgentById(Long id);

    List<ActiTaskAgent> selectActiTaskAgentByCreateUserAndEndTime(@Param("tenantKey") String tenantKey, @Param("createUser") String createUser, @Param("endTime") Date endTime);

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
     * 删除代理
     * 
     * @param id 代理ID
     * @return 结果
     */
    int deleteActiTaskAgentById(Long id);

    /**
     * 批量删除代理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTaskAgentByIds(String[] ids);
}
