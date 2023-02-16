package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.po.ActiTodoTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>描 述： ActiTodoTaskMapper
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface ActiTodoTaskMapper {
    /**
     * 查询待办任务
     *
     * @param id 待办任务ID
     * @return 待办任务
     */
    ActiTodoTask selectActiTodoTaskById(Long id);

    ActiTodoTask selectActiTodoTaskByTaskId(String taskId);

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
     * 删除待办任务
     *
     * @param id 待办任务ID
     * @return 结果
     */
    int deleteActiTodoTaskById(Long id);

    /**
     * 批量删除待办任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiTodoTaskByIds(String[] ids);

    int deleteActiTodoTaskByTaskId(String taskId);

    int updateReceiverByTaskId(@Param("taskId") String taskId, @Param("receiver") String assignee);
}
