package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.po.ActiTodoTask;

import java.util.List;

/**
 * <br>描 述： IActiTodoTaskService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActiTodoTaskService {
    /**
     * 查询待办任务
     *
     * @param id 待办任务ID
     * @return 待办任务
     */
    ActiTodoTask selectActiTodoTaskById(Long id);

    /**
     * 查询待办任务列表
     *
     * @param actiTodoTask 待办任务
     * @return 待办任务集合
     */
    List<ActiTodoTask> selectActiTodoTaskList(ActiTodoTask actiTodoTask);

    /**
     * 新增待办任务
     *
     * @param actiTodoTask 待办任务
     * @return 结果
     */
    int insertActiTodoTask(ActiTodoTask actiTodoTask);

    /**
     * 修改待办任务
     *
     * @param actiTodoTask 待办任务
     * @return 结果
     */
    int updateActiTodoTask(ActiTodoTask actiTodoTask);

    /**
     * 批量删除待办任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTodoTaskByIds(String ids);

    /**
     * 删除待办任务信息
     *
     * @param id 待办任务ID
     * @return 结果
     */
    int deleteActiTodoTaskById(Long id);

    int deleteActiTodoTaskByTaskId(String taskId);

    ActiTodoTask selectActiTodoTaskByTaskId(String taskId);

    int updateReceiverByTaskId(String taskId, String assignee);
}
