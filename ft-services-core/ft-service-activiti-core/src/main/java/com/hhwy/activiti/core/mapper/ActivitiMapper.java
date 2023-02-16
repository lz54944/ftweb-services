package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.dto.ActRunTaskDTO;
import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper接口
 *
 * @author jzq
 * @date 2021-07-28
 */
public interface ActivitiMapper {

    String getProcessDefinitionIdByDeploymentId(String deploymentId);

    String getProcessKeyByDefinitionId(String definitionId);

    List<String> getProcessInstanceIdByDefinitionId(String definitionId);

    String getProcessDefinitionIdByInstanceId(String instanceId);

    List<ActRunTaskDTO> selectRunningTasksByDefinitionId(String definitionId);

    String getTaskIdByProcInstIdAndTaskDefKey(@Param("procInstId") String procInstId, @Param("taskDefKey") String taskDefKey);

    List<String> getTaskIdListByProcInstIdAndTaskDefKey(@Param("procInstId") String procInstId, @Param("taskDefKey") String taskDefKey);

    String getTaskNameByProcInstIdAndTaskDefKey(@Param("procInstId") String procInstId, @Param("taskDefKey") String taskDefKey);

    List<ActiTaskConfig> getCompleteNodesByInstanceId(String instanceId);

    List<HistoricTaskInstanceEntityImpl> selectHistoricTaskByInstanceId(String instanceId);

    List<String> getRecallTaskIds(String procInstId);

    List<ActiTodoTaskHistory> selectTaskTraces(String instanceId);
}
