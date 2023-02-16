package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;

import java.util.List;

/**
 * 已办任务Mapper接口
 * 
 * @author jzq
 * @date 2021-08-03
 */
public interface ActiTodoTaskHistoryMapper {
    /**
     * 查询已办任务
     * 
     * @param id 已办任务ID
     * @return 已办任务
     */
    ActiTodoTaskHistory selectActiTodoTaskHistoryById(Long id);


    ActiTodoTaskHistory selectActiTodoTaskHistoryByNextTaskId(String nextTaskId);

    ActiTodoTaskHistory selectActiTodoTaskHistoryByPreTaskId(String preTaskId);

    ActiTodoTaskHistory selectActiTodoTaskHistoryByTaskId(String taskId);

    /**
     * 查询已办任务列表
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 已办任务集合
     */
    List<ActiTodoTaskHistory> selectActiTodoTaskHistoryList(ActiTodoTaskHistory actiTodoTaskHistory);

    /**
     * 新增已办任务
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 结果
     */
    int insertActiTodoTaskHistory(ActiTodoTaskHistory actiTodoTaskHistory);

    /**
     * 修改已办任务
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 结果
     */
    int updateActiTodoTaskHistory(ActiTodoTaskHistory actiTodoTaskHistory);

    /**
     * 删除已办任务
     * 
     * @param id 已办任务ID
     * @return 结果
     */
    int deleteActiTodoTaskHistoryById(Long id);

    /**
     * 批量删除已办任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTodoTaskHistoryByIds(String[] ids);

    int setRecallTask(String taskId);

    int setBackTask(String taskId);
}
