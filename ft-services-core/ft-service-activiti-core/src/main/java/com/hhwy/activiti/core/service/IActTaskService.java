package com.hhwy.activiti.core.service;

import com.github.pagehelper.Page;
import com.hhwy.activiti.core.domain.dto.ActTaskDTO;
import com.hhwy.activiti.core.domain.dto.ActWorkflowFormDataDTO;
import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.PageDomain;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： IActTaskService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActTaskService {

    TaskEntityImpl startProcess(String processKey, String businessId, Map<String, Object> variables);

    //void complete(HttpServletRequest request, String taskId);

    void firstNodeSubmit(String businessId, String processKey, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables);

    void spBack(String instanceId, String currentTaskId, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables);

    void submit(String instanceId, String currentTaskId, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables);

    List<ActiTaskConfig> nextNodes(String processKey, String taskId, Map<String, Object> variables);

    AjaxResult receivers(String processKey, String instanceId, String taskNodeId);

    Page<ActTaskDTO> selectProcessDefinitionList(PageDomain pageDomain);

    List<String> formDataShow(String taskID);

    int formDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs) throws ParseException;

    void back(String taskId, String comment, Map<String,Object> variables) throws Exception;

    void recall(String taskId);

    List<ActiTaskConfig> completeNodes(String instanceId);

    void suspended(String instanceId);

    void activate(String instanceId);

    void deleteProcessInstance(String instanceId, String deleteReason);

    void reassign(String taskId, String assignee);

    List<ActiTodoTaskHistory> getTaskTraces(String instanceId);
}
