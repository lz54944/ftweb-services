package com.hhwy.activiti.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.activiti.core.domain.dto.ActTaskDTO;
import com.hhwy.activiti.core.domain.dto.ActWorkflowFormDataDTO;
import com.hhwy.activiti.core.domain.po.*;
import com.hhwy.activiti.core.mapper.ActiBusinessInfoMapper;
import com.hhwy.activiti.core.mapper.ActiTaskAgentMapper;
import com.hhwy.activiti.core.mapper.ActiTaskConfigMapper;
import com.hhwy.activiti.core.mapper.ActivitiMapper;
import com.hhwy.activiti.core.service.IActTaskService;
import com.hhwy.activiti.core.service.IActiFormDataService;
import com.hhwy.activiti.core.service.IActiTodoTaskHistoryService;
import com.hhwy.activiti.core.service.IActiTodoTaskService;
import com.hhwy.activiti.core.utils.UserService;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.UUIDUtils;
import com.hhwy.common.core.utils.bean.BeanUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.PageDomain;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.RemoteUserService;
import com.hhwy.system.api.domain.SysDept;
import com.hhwy.system.api.domain.SysUser;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <br>描 述： ActTaskServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private IActiFormDataService actWorkflowFormDataService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ActivitiMapper activitiMapper;

    @Autowired
    private ActiTaskAgentMapper actiTaskAgentMapper;

    @Autowired
    private IActiTodoTaskService actiTodoTaskService;

    @Autowired
    private IActiTodoTaskHistoryService actiTodoTaskHistoryService;

    @Autowired
    private ActiTaskConfigMapper actiTaskConfigMapper;

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActiBusinessInfoMapper actiBusinessInfoMapper;

    @Override
    @Transactional
    public TaskEntityImpl startProcess(String processKey, String businessId, Map<String, Object> variables) {
        // 启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processKey, businessId, variables);
        TaskEntityImpl firstTask = (TaskEntityImpl) taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        String firstTaskId = firstTask.getId();
        //taskService.setAssignee(firstTaskId,tokenService.getLoginUser().getUsername());
        taskService.claim(firstTaskId, tokenService.getLoginUser().getUsername());
        ActiTodoTask actiTodoTask = initActiTodoTask(firstTaskId,null);
        insertActiTodoTask(actiTodoTask);
        insertActiBusinessInfo(actiTodoTask);
        return firstTask;
    }

    @Override
    @Transactional
    public void firstNodeSubmit(String businessId, String processKey, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables) {
        Assert.notNull(processKey,"流程key不能为空");
        TaskEntityImpl firstTask = startProcess(processKey, businessId, variables);
        submit(firstTask.getProcessInstanceId(), firstTask.getId(), nextNodeId, nextTaskAssignee, comment, variables);
    }

    @Override
    @Transactional
    public void submit(String instanceId, String currentTaskId, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables) {
        Assert.notNull(instanceId,"流程实例id不能为空");
        Assert.notNull(currentTaskId,"当前任务id不能为空");
        Assert.notNull(nextNodeId,"下一步节点不能为空");
        ProcessInstance instance = getProcessInstance(instanceId);
        if (instance.isSuspended()) {
            throw new RuntimeException("流程已被挂起!");
        }
        SysUser sysUser = tokenService.getSysUser();
        String currentUserName = sysUser.getUserName();
        if(StringUtils.isNotEmpty(comment)){
            taskService.addComment(currentTaskId,instanceId,comment);
        }
        /*// 被委派人处理完成任务 被委托的流程需要先 resolved 这个任务再提交  所以在 complete 之前需要先 resolved, resolveTask() 要在 claim() 之前 不然 act_hi_taskinst 表的 assignee 字段会为 null
         //将任务委托给另一个用户
        taskService.delegateTask(currentTaskId,"被委托人");
        //配合delegateTask使用  标志着受让人完成了这项任务，提供了所需的变量，并且可以将其发回给所有者
        taskService.resolveTask(currentTaskId, variables);*/

        org.activiti.engine.task.Task currentTask = taskService.createTaskQuery().taskId(currentTaskId).singleResult();
        Assert.notNull(currentTask,"currentTaskId输入错误");
        String agentUser = null;
        try {
            // 再次拾取任务，如果报错，则说明该任务不归属当前用户
            taskService.claim(currentTaskId, currentUserName);
        }catch (Exception e){
            List<ActiTaskAgent> actiTaskAgentList = actiTaskAgentMapper.selectActiTaskAgentByCreateUserAndEndTime(tokenService.getTenantKey(),currentUserName, new Date());
            String assignee = currentTask.getAssignee();
            List<ActiTaskAgent> collect = actiTaskAgentList.stream().filter(a -> a.getAgentUser().equals(assignee)).collect(Collectors.toList());
            if (!sysUser.isAdmin() && CollectionUtils.isEmpty(collect)) {
                e.printStackTrace();
                throw new RuntimeException("本条任务应该由`"+assignee+"`处理,`"+currentUserName+"`无权处理");
            }else {
                agentUser = currentUserName;
            }
        }
        nextNodeId = complete(currentTask, nextNodeId, variables);

        ParallelMultiInstanceBehavior multiInstanceInfo = multiInstanceInfo(currentTask.getProcessDefinitionId(), nextNodeId);
        if(multiInstanceInfo==null){ // 下一节点为普通流程节点
            String nextTaskId = activitiMapper.getTaskIdByProcInstIdAndTaskDefKey(instanceId,nextNodeId);
            if(StringUtils.isNotEmpty(nextTaskId)){
                Assert.notNull(nextTaskAssignee,"下一步处理人不能为空");
                // 处理待办、已办
                taskService.claim(nextTaskId, nextTaskAssignee);
                moveTodoTask2HistoryAndInsertTodoTaskAndInsertBusinessInfo(currentTaskId,nextTaskId,agentUser);
            }else {
                if(isEndNode(currentTask.getProcessDefinitionId(), nextNodeId)){//结束流程
                    moveTodoTask2HistoryAndInsertTodoTaskAndInsertBusinessInfo(currentTaskId,nextTaskId,agentUser);
                }else {
                    Assert.notNull(nextTaskAssignee,"下一步处理人不能为空");
                    ParallelMultiInstanceBehavior parallelMultiInstanceBehavior = multiInstanceInfo(currentTask.getProcessDefinitionId(), currentTask.getTaskDefinitionKey());
                    if(parallelMultiInstanceBehavior!=null){//当前节点为会签
                        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
                        FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(nextNodeId);
                        moveTodoTask2History(currentTaskId,null,nextNodeId,flowElement.getName(),agentUser);
                    }else {
                        Assert.notNull(nextTaskId,"nextTaskId不能为空");
                    }
                }
            }
        }else{ // 下一节点为会签流程节点
            List<String> nextTaskIdList = activitiMapper.getTaskIdListByProcInstIdAndTaskDefKey(instanceId, nextNodeId);
            String collectionVariable = multiInstanceInfo.getCollectionVariable();
            List<String> assignList = (List<String>) variables.get(collectionVariable);
            if (CollectionUtils.isEmpty(assignList) || (nextTaskIdList.size() != assignList.size())) {
                throw new RuntimeException("会签人数异常");
            }
            for (int i = 0; i < nextTaskIdList.size(); i++) {
                String nextTaskId = nextTaskIdList.get(i);
                nextTaskAssignee = assignList.get(i);
                if(StringUtils.isNotEmpty(nextTaskId)){
                    // 处理待办、已办
                    taskService.claim(nextTaskId, nextTaskAssignee);
                    moveTodoTask2HistoryAndInsertTodoTaskAndInsertBusinessInfo(currentTaskId,nextTaskId,agentUser);
                }else {
                    if(isEndNode(currentTask.getProcessDefinitionId(), nextNodeId)){//结束流程
                        moveTodoTask2HistoryAndInsertTodoTaskAndInsertBusinessInfo(currentTaskId,nextTaskId,agentUser);
                    }else {
                        Assert.notNull(nextTaskId,"nextTaskId不能为空");
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void spBack(String instanceId, String currentTaskId, String nextNodeId, String nextTaskAssignee, String comment, Map<String, Object> variables) {
        submit(instanceId, currentTaskId, nextNodeId, nextTaskAssignee, comment, variables);
        ActiTodoTaskHistory preActiTodoTaskHistory = actiTodoTaskHistoryService.selectActiTodoTaskHistoryByTaskId(currentTaskId);
        actiTodoTaskHistoryService.setBackTask(preActiTodoTaskHistory.getTaskId());
    }

    //当前TaskEntity为执行期的manage中科查询到的对象
    private ParallelMultiInstanceBehavior multiInstanceInfo(String processDefinitionId, String taskDefinitionKey) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 获取当前节点
        FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
        // 根据活动id获取活动实例
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            if (userTask.getBehavior() instanceof ParallelMultiInstanceBehavior) {
                ParallelMultiInstanceBehavior behavior = (ParallelMultiInstanceBehavior) userTask.getBehavior();
                if (behavior != null ) {
                    return behavior;
                }
            }
        }
        return null;
    }

    public ProcessInstance getProcessInstance(String processInstanceId){
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    public boolean isEndNode(String processDefinitionId, String nodeId){
        FlowElement flowElement = repositoryService.getBpmnModel(processDefinitionId).getProcesses().get(0).getFlowElement(nodeId);
        if(flowElement instanceof EndEvent){
            return true;
        }else {
            return false;
        }
    }

    public boolean isEndNode(FlowElement flowElement){
        if(flowElement instanceof EndEvent){
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public String complete(org.activiti.engine.task.Task task, String nextTaskNodeId, Map<String, Object> variables){
        String processDefinitionId = task.getProcessDefinitionId();
        String currentTaskId = task.getId();
        String currentTaskNodeId = task.getTaskDefinitionKey();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 获取当前节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentTaskNodeId);
        //会签处理
        nextTaskNodeId = multiInstanceVarResolver(task, currentFlowNode, nextTaskNodeId);
        //记录当前节点的原活动方向
        List<SequenceFlow> oldOutgoingFlows = new ArrayList<>();
        oldOutgoingFlows.addAll(currentFlowNode.getOutgoingFlows());
        //清理活动方向
        currentFlowNode.getOutgoingFlows().clear();
        try {
            // 获取下一个节点
            FlowNode nextFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(nextTaskNodeId);
            //建立新活动方向
            List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
            SequenceFlow newSequenceFlow = new SequenceFlow();
            newSequenceFlow.setId(UUIDUtils.getID());
            newSequenceFlow.setSourceFlowElement(currentFlowNode);
            newSequenceFlow.setTargetFlowElement(nextFlowNode);
            newSequenceFlowList.add(newSequenceFlow);
            //设置新活动方向
            currentFlowNode.setOutgoingFlows(newSequenceFlowList);
            // 完成任务
            taskService.setVariables(currentTaskId, variables);
            taskService.complete(currentTaskId, variables);
        }finally {
            //恢复原方向
            currentFlowNode.setOutgoingFlows(oldOutgoingFlows);
        }
        return nextTaskNodeId;
    }

    public String multiInstanceVarResolver(org.activiti.engine.task.Task currentTask, FlowNode currentFlowNode, String nextTaskNodeId){
        List<SequenceFlow> outgoingFlows = currentFlowNode.getOutgoingFlows();
        String processDefinitionId = currentTask.getProcessDefinitionId();
        String taskNodeId = currentTask.getTaskDefinitionKey();
        String currentConditionExpression = null;

        ParallelMultiInstanceBehavior multiInstanceInfo = multiInstanceInfo(processDefinitionId, taskNodeId);
        if(multiInstanceInfo!=null){//当前节点为会签节点
            for (SequenceFlow outgoingFlow : outgoingFlows) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    String conditionExpression = outgoingFlow.getConditionExpression();
                    String nextTaskEleNodeId = outgoingFlow.getTargetFlowElement().getId();
                    if(nextTaskNodeId.equals(nextTaskEleNodeId)){
                        currentConditionExpression = conditionExpression;
                        break;
                    }
                }
                FlowNode targetFlowElement = (FlowNode) outgoingFlow.getTargetFlowElement();
                if (targetFlowElement instanceof ExclusiveGateway){
                    multiInstanceVarResolver(currentTask, targetFlowElement, nextTaskNodeId);
                }
            }
            if(StringUtils.isNotEmpty(currentConditionExpression)){
                String conditionVarName = getConditionExpressionNumName(currentConditionExpression);
                int num = getConditionExpressionNumVal(conditionVarName,currentTask.getExecutionId());
                runtimeService.setVariable(currentTask.getExecutionId(), conditionVarName,num+1);
            }

            // TODO: 2021/8/24  1、获取当前会签节点的条件  判断是否满足 如果满足  计算出线条件 指向符合条件的下一个节点
            if (currentFlowNode instanceof UserTask){
                Integer nrOfInstances = (Integer)runtimeService.getVariable(currentTask.getExecutionId(), "nrOfInstances");
                Integer nrOfActiveInstances = (Integer)runtimeService.getVariable(currentTask.getExecutionId(), "nrOfActiveInstances")-1;
                Integer nrOfCompletedInstances = (Integer)runtimeService.getVariable(currentTask.getExecutionId(), "nrOfCompletedInstances")+1;
                String completionCondition = ((UserTask) currentFlowNode).getLoopCharacteristics().getCompletionCondition();
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                context.setVariable("nrOfInstances", factory.createValueExpression(nrOfInstances, nrOfInstances.getClass()));
                context.setVariable("nrOfActiveInstances", factory.createValueExpression(nrOfActiveInstances, nrOfActiveInstances.getClass()));
                context.setVariable("nrOfCompletedInstances", factory.createValueExpression(nrOfCompletedInstances, nrOfCompletedInstances.getClass()));
                ValueExpression ve = factory.createValueExpression(context, completionCondition, boolean.class);
                if ((Boolean) ve.getValue(context)) {
                    int maxConditionExpressionNumVal=0;
                    Map<String, String> allConditionExpressionMap = getAllConditionExpressionMap(currentFlowNode,new HashMap<>());
                    for (Map.Entry<String, String> conditionExpressionEntry : allConditionExpressionMap.entrySet()) {
                        String conditionExpression = conditionExpressionEntry.getKey();
                        String taskEleNodeId = conditionExpressionEntry.getValue();
                        int conditionExpressionNumVal = getConditionExpressionNumVal(getConditionExpressionNumName(conditionExpression), currentTask.getExecutionId());
                        if(conditionExpressionNumVal>maxConditionExpressionNumVal){
                            maxConditionExpressionNumVal = conditionExpressionNumVal;
                            nextTaskNodeId = taskEleNodeId;
                        }
                    }
                    return nextTaskNodeId;
                }
            }
        }
        return nextTaskNodeId;
    }

    public Map<String, String> getAllConditionExpressionMap(FlowNode flowNode, Map<String,String> conditionExpressionMap){
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                String conditionExpression = outgoingFlow.getConditionExpression();
                String nextTaskEleNodeId = outgoingFlow.getTargetFlowElement().getId();
                conditionExpressionMap.put(conditionExpression,nextTaskEleNodeId);
            }
            FlowNode targetFlowElement = (FlowNode) outgoingFlow.getTargetFlowElement();
            if (targetFlowElement instanceof ExclusiveGateway){
                getAllConditionExpressionMap(targetFlowElement, conditionExpressionMap);
            }
        }
        return conditionExpressionMap;
    }

    public int getConditionExpressionNumVal(String conditionVar, String executionId){
        int num = 0;
        Object numObj = runtimeService.getVariable(executionId, conditionVar);
        if(numObj!=null){
            num = (int)numObj;
        }
        return num;
    }
    public String getConditionExpressionNumName(String conditionExpression){
        String conditionVarName = conditionVarResolver(conditionExpression)+"Num";
        return conditionVarName;
    }

    public String conditionVarResolver(String conditionExpression){
        boolean startBool = conditionExpression.startsWith("${");
        if(startBool){
            boolean endBool = conditionExpression.endsWith("}");
            if(!endBool){
                throw new RuntimeException("条件："+conditionExpression+" 格式错误");
            }
        }else {
            throw new RuntimeException("条件："+conditionExpression+" 格式错误");
        }
        String conditionVar = conditionExpression.substring(2, conditionExpression.length()-1);
        return conditionVar;
    }

    @Transactional
    public void backProcess(String taskId) throws Exception {
        ActiTodoTaskHistory actiTodoTaskHistory = actiTodoTaskHistoryService.selectActiTodoTaskHistoryByNextTaskId(taskId);
        // 上一个任务
        HistoricTaskInstance lastTask = historyService.createHistoricTaskInstanceQuery().taskId(actiTodoTaskHistory.getTaskId()).singleResult();;
        // 当前任务
        HistoricTaskInstance curTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();;
        // 当前节点的executionId
        String curExecutionId = curTask.getExecutionId();
        // 上个节点的taskId
        String lastTaskId = lastTask.getId();
        // 上个节点的executionId
        String lastExecutionId = lastTask.getExecutionId();
        if (null == lastTaskId) {
            throw new Exception("LAST TASK IS NULL");
        }
        String processDefinitionId = lastTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String lastActivityId = null;
        List<HistoricActivityInstance> haiFinishedList = historyService.createHistoricActivityInstanceQuery()
                .executionId(lastExecutionId).finished().list();
        for (HistoricActivityInstance hai : haiFinishedList) {
            if (lastTaskId.equals(hai.getTaskId())) {
                // 得到ActivityId，只有HistoricActivityInstance对象里才有此方法
                lastActivityId = hai.getActivityId();
                break;
            }
        }
        // 得到上个节点的信息
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(lastActivityId);
        // 取得当前节点的信息
        Execution execution = runtimeService.createExecutionQuery().executionId(curExecutionId).singleResult();
        String curActivityId = execution.getActivityId();
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActivityId);
        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());
        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId(UUIDUtils.getID());
        newSequenceFlow.setSourceFlowElement(curFlowNode);
        newSequenceFlow.setTargetFlowElement(lastFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        curFlowNode.setOutgoingFlows(newSequenceFlowList);
        // 完成任务
        taskService.complete(taskId);
        //恢复原方向
        curFlowNode.setOutgoingFlows(oriSequenceFlows);
        org.activiti.engine.task.Task nextTask = taskService.createTaskQuery()
                .processInstanceId(actiTodoTaskHistory.getProcessInstanceId()).taskDefinitionKey(lastTask.getTaskDefinitionKey()).singleResult();
        // 设置执行人
        taskService.claim(nextTask.getId(), lastTask.getAssignee());
    }

    @Transactional
    public void moveTodoTask2HistoryAndInsertTodoTaskAndInsertBusinessInfo(String currentTaskId, String nextTaskId, String agentUser){
        ActiTodoTask futureActiTodoTask = new ActiTodoTask();
        if(StringUtils.isEmpty(nextTaskId)){
            futureActiTodoTask.setPreTaskId(currentTaskId);
            futureActiTodoTask.setTaskNodeName("end");
        }else {
            futureActiTodoTask = initActiTodoTask(nextTaskId,currentTaskId);
        }
        ActiTodoTask actiTodoTask = actiTodoTaskService.selectActiTodoTaskByTaskId(currentTaskId);
        if(actiTodoTask != null){
            // 已办事项
            copyActiTodoTask2History(actiTodoTask, futureActiTodoTask, agentUser);
            // 删除上调待办事项
            deleteActiTodoTaskById(actiTodoTask.getId());
        }
        if(StringUtils.isNotEmpty(nextTaskId)){
            // 待办事项
            insertActiTodoTask(futureActiTodoTask);
        }
        insertActiBusinessInfo(futureActiTodoTask);
    }

    public void insertActiBusinessInfo(ActiTodoTask actiTodoTask){
        String executionId;
        String businessId;
        String businessTableName;
        ActiBusinessInfo oldActiBusinessInfo;
        ActiBusinessInfo actiBusinessInfo = new ActiBusinessInfo();
        actiBusinessInfo.setFormUrl(actiTodoTask.getFormUrl());
        String tenantKey = tokenService.getTenantKey();
        if(StringUtils.isEmpty(actiTodoTask.getTaskId())){//结束节点
            businessId = (String)historyService.createHistoricVariableInstanceQuery().taskId(actiTodoTask.getPreTaskId()).variableName("businessId").singleResult().getValue();
            businessTableName = (String)historyService.createHistoricVariableInstanceQuery().taskId(actiTodoTask.getPreTaskId()).variableName("businessTableName").singleResult().getValue();
            oldActiBusinessInfo = actiBusinessInfoMapper.selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(businessTableName, businessId, tenantKey);
            actiBusinessInfo.setProcessNodeId("-");
            actiBusinessInfo.setProcessTaskId("-");
            actiBusinessInfo.setProcessTaskName(actiTodoTask.getTaskNodeName());
            actiBusinessInfo.setProcessTaskMan("-");
            actiBusinessInfo.setRemark("completed");
        }else {
            executionId = taskService.createTaskQuery().taskId(actiTodoTask.getTaskId()).singleResult().getExecutionId();
            businessId = (String) runtimeService.getVariable(executionId, "businessId");
            businessTableName = (String) runtimeService.getVariable(executionId, "businessTableName");
            oldActiBusinessInfo = actiBusinessInfoMapper.selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(businessTableName, businessId, tenantKey);
            actiBusinessInfo.setProcessNodeId(actiTodoTask.getTaskNodeId());
            actiBusinessInfo.setProcessTaskId(actiTodoTask.getTaskId());
            actiBusinessInfo.setProcessTaskName(actiTodoTask.getTaskNodeName());
            actiBusinessInfo.setProcessTaskMan(actiTodoTask.getReceiver());
        }

        if(oldActiBusinessInfo == null){
            actiBusinessInfo.setBusinessId(businessId);
            actiBusinessInfo.setBusinessTableName(businessTableName);
            actiBusinessInfo.setProcessInstanceId(actiTodoTask.getProcessInstanceId());
            actiBusinessInfo.setCreateTime(DateUtils.getNowDate());
            actiBusinessInfo.setTenantKey(tenantKey);
            actiBusinessInfoMapper.insertActiBusinessInfo(actiBusinessInfo);
        }else {
            actiBusinessInfo.setId(oldActiBusinessInfo.getId());
            actiBusinessInfo.setUpdateTime(DateUtils.getNowDate());
            actiBusinessInfoMapper.updateActiBusinessInfo(actiBusinessInfo);
        }

    }

    @Transactional
    public void moveTodoTask2History(String currentTaskId, String nextTaskId,String nextTaskNodeId, String nextTaskNodeName, String agentUser){
        ActiTodoTask futureActiTodoTask = new ActiTodoTask();
        futureActiTodoTask.setTaskId(nextTaskId);
        futureActiTodoTask.setTaskNodeId(nextTaskNodeId);
        futureActiTodoTask.setTaskNodeName(nextTaskNodeName);
        ActiTodoTask actiTodoTask = actiTodoTaskService.selectActiTodoTaskByTaskId(currentTaskId);
        if(actiTodoTask != null){
            // 已办事项
            copyActiTodoTask2History(actiTodoTask, futureActiTodoTask, agentUser);
            // 删除上调待办事项
            deleteActiTodoTaskById(actiTodoTask.getId());
        }
    }

    @Transactional
    public ActiTodoTask initActiTodoTask(String currentTaskId,String preTaskId){
        String tenantKey = tokenService.getTenantKey();
        TaskEntityImpl task = (TaskEntityImpl) taskService.createTaskQuery().taskId(currentTaskId).singleResult();
        ActiTaskConfig actiTaskConfig = actiTaskConfigMapper.selectActiTaskConfigByProcDefIdAndTaskNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(),tenantKey);
        if (actiTaskConfig==null && !Constants.MASTER_TENANT_KEY.equals(tenantKey)) {
            actiTaskConfig = actiTaskConfigMapper.selectActiTaskConfigByProcDefIdAndTaskNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), Constants.MASTER_TENANT_KEY);
        }
        String formUrl = actiTaskConfig==null ? null : actiTaskConfig.getFormUrl();
        Assert.notNull(formUrl,"请先配置`"+task.getTaskDefinitionKey()+"`节点的表单处理地址");
        ProcessDefinitionEntityImpl processDefinition = (ProcessDefinitionEntityImpl) repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        Map<String, Object> variablesMap = taskService.getVariables(task.getId());
        String variables = null;
        String title = null;
        if(variablesMap!=null){
            variables = JSON.toJSONString(variablesMap);
            title = (String) variablesMap.get("title");
            if (title == null) {
                title =  processDefinition.getName();
            }
        }
        String preTaskNodeId;
        String preTaskNodeName;
        if (StringUtils.isEmpty(preTaskId)) {
            FlowElement initialFlowElement = repositoryService.getBpmnModel(task.getProcessDefinitionId()).getMainProcess().getInitialFlowElement();
            //该节点为第一个节点
            preTaskNodeId = initialFlowElement.getId();
            preTaskNodeName = initialFlowElement.getName();
        }else {
            //如果不是第一个节点
            HistoricTaskInstance preTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(preTaskId).singleResult();
            preTaskNodeId = preTaskInstance.getTaskDefinitionKey();
            preTaskNodeName = preTaskInstance.getName();
        }

        ActiTodoTask actiTodoTask = new ActiTodoTask();
        actiTodoTask.setTitle(title);
        actiTodoTask.setBusinessId(task.getBusinessKey());
        actiTodoTask.setReceiver(task.getAssignee());
        actiTodoTask.setSender(tokenService.getLoginUser().getUsername());
        actiTodoTask.setProcessInstanceId(task.getProcessInstanceId());
        actiTodoTask.setProcessKey(processDefinition.getKey());
        actiTodoTask.setProcessName(processDefinition.getName());
        actiTodoTask.setTaskId(task.getId());
        actiTodoTask.setTaskNodeId(task.getTaskDefinitionKey());
        actiTodoTask.setTaskNodeName(task.getName());
        actiTodoTask.setTaskState(task.getSuspensionState());
        actiTodoTask.setTimeLimit(task.getDueDate());
        actiTodoTask.setTaskStartTime(task.getCreateTime());
        actiTodoTask.setFormUrl(formUrl);
        actiTodoTask.setVariables(variables);
        actiTodoTask.setPreTaskId(preTaskId);
        actiTodoTask.setPreTaskNodeId(preTaskNodeId);
        actiTodoTask.setPreTaskNodeName(preTaskNodeName);
        actiTodoTask.setRemark(task.getDescription());
        actiTodoTask.setTenantKey(task.getTenantId());
        return actiTodoTask;
    }

    @Transactional
    public void copyActiTodoTask2History(ActiTodoTask actiTodoTask,String nextTaskId,String nextTaskNodeId, String nextTaskNodeName, String agentUser){
        ActiTodoTaskHistory actiTodoTaskHistory = new ActiTodoTaskHistory();
        BeanUtils.copyBeanProp(actiTodoTaskHistory, actiTodoTask);
        actiTodoTaskHistory.setTaskEndTime(new Date());
        actiTodoTaskHistory.setAgentUser(agentUser);
        actiTodoTaskHistory.setNextTaskId(nextTaskId);
        actiTodoTaskHistory.setNextTaskNodeId(nextTaskNodeId);
        actiTodoTaskHistory.setNextTaskNodeName(nextTaskNodeName);
        actiTodoTaskHistoryService.insertActiTodoTaskHistory(actiTodoTaskHistory);
    }

    @Transactional
    public void copyActiTodoTask2History(ActiTodoTask actiTodoTask, ActiTodoTask nextTask, String agentUser){
        copyActiTodoTask2History(actiTodoTask,nextTask.getTaskId(),nextTask.getTaskNodeId(),nextTask.getTaskNodeName(), agentUser);
    }

    @Transactional
    public void deleteActiTodoTaskById(Long todoTaskId){
        actiTodoTaskService.deleteActiTodoTaskById(todoTaskId);
    }

    @Transactional
    public void insertActiTodoTask(ActiTodoTask actiTodoTask){
        actiTodoTaskService.insertActiTodoTask(actiTodoTask);
    }

    @Override
    public List<ActiTaskConfig> nextNodes(String processKey, String taskId, Map<String, Object> variables) {
        List<ActiTaskConfig> result = new ArrayList<>();
        if (StringUtils.isBlank(taskId)) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
            Assert.notNull(processDefinition,"流程尚未部署");
            String processDefinitionId = processDefinition.getId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            //Process process = bpmnModel.getProcesses().get(0);
            Process process = bpmnModel.getMainProcess();
            // 找到当前任务的流程变量
            FlowNode initialFlowNode = (FlowNode) process.getInitialFlowElement();
            List<FlowNode> nextNodes = new ArrayList<>();
            iteratorNextNodes(process, initialFlowNode, nextNodes, new ArrayList<>(), variables);

            for (FlowNode nextNode : nextNodes) {
                for (SequenceFlow outgoingFlow : nextNode.getOutgoingFlows()) {
                    FlowElement flowElement = outgoingFlow.getTargetFlowElement();
                    ActiTaskConfig actiTaskConfig = new ActiTaskConfig();
                    actiTaskConfig.setTaskNodeId(flowElement.getId());
                    actiTaskConfig.setTaskNodeName(flowElement.getName());
                    actiTaskConfig.setProcDefId(processDefinitionId);
                    actiTaskConfig.setProcDefKey(processDefinition.getKey());
                    result.add(actiTaskConfig);
                }
            }
            return result;
        } else {
            org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Assert.notNull(task,"taskId输入错误");
            List<FlowNode> nextTask = findNextTask(taskId, variables);
            for (FlowNode flowNode : nextTask) {
                ActiTaskConfig actiTaskConfig = new ActiTaskConfig();
                actiTaskConfig.setTaskNodeId(flowNode.getId());
                actiTaskConfig.setTaskNodeName(flowNode.getName());
                actiTaskConfig.setProcDefId(task.getProcessDefinitionId());
                if(isEndNode(flowNode)){
                    actiTaskConfig.setIsEnd(true);
                }
                result.add(actiTaskConfig);
            }
            return result;
        }
    }

    @Override
    public AjaxResult receivers(String processKey, String instanceId, String taskNodeId) {
        String processDefinitionId;
        if(StringUtils.isNotEmpty(instanceId)){
            processDefinitionId = activitiMapper.getProcessDefinitionIdByInstanceId(instanceId);
        }else {
            Assert.notNull(processKey,"第一次发起流程，key不能为空；若非首次发起，则要保证流程实例id不能为空");
            processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult().getId();
        }
        String tenantKey = tokenService.getTenantKey();
        ActiTaskConfig actiTaskConfig = actiTaskConfigMapper.selectActiTaskConfigByProcDefIdAndTaskNodeId(processDefinitionId, taskNodeId, tenantKey);
        if(actiTaskConfig==null && !Constants.MASTER_TENANT_KEY.equals(tenantKey)) {
            actiTaskConfig = actiTaskConfigMapper.selectActiTaskConfigByProcDefIdAndTaskNodeId(processDefinitionId, taskNodeId, Constants.MASTER_TENANT_KEY);
        }
        String assignees = actiTaskConfig.getAssignees();
        if(StringUtils.isEmpty(assignees)){
            return AjaxResult.error("请先配置任务参与者！");
        }else {
            String assigneeType = actiTaskConfig.getAssigneeType(); //配置类型(1：角色:2：人员)
            if("1".equals(assigneeType)){
                String assigneesScope = actiTaskConfig.getAssigneesScope(); //处理人范围（1：全部  2：本部门）
                SysUser sysUser = tokenService.getSysUser();
                if (!sysUser.isAdmin() && "2".equals(assigneesScope)) {
                    String deptIds = sysUser.getDeptId()+"";
                    List<SysDept> partDeptList = sysUser.getPartDeptList(); //兼职部门
                    if (CollectionUtils.isNotEmpty(partDeptList)) {
                        for (SysDept sysDept : partDeptList) {
                            deptIds = deptIds + "," + sysDept.getDeptId();
                        }
                    }
                    List<SysUser> data = remoteUserService.getUserListByOriginalRoleIdsAndDeptIds(assignees, deptIds).getData();
                    return AjaxResult.success(data);
                }else {
                    List<SysUser> data = remoteUserService.getUserListByOriginalRoleIds(assignees).getData();
                    return AjaxResult.success(data);
                }
            }
            if("2".equals(assigneeType)){
                List<SysUser> data = remoteUserService.getUserListByOriginalUserIds(assignees).getData();
                return AjaxResult.success(data);
            }
            return null;
        }
    }

    @Override
    public Page<ActTaskDTO> selectProcessDefinitionList(PageDomain pageDomain) {
        Page<ActTaskDTO> list = new Page<>();
        org.activiti.api.runtime.shared.query.Page<Task> pageTasks = taskRuntime.tasks(Pageable.of((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize()));
        List<Task> tasks = pageTasks.getContent();
        int totalItems = pageTasks.getTotalItems();
        list.setTotal(totalItems);
        if (totalItems != 0) {
            Set<String> processInstanceIdIds = tasks.parallelStream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdIds).list();
            List<ActTaskDTO> actTaskDTOS = tasks.stream()
                    .map(t -> new ActTaskDTO(t, processInstanceList.parallelStream().filter(pi -> t.getProcessInstanceId().equals(pi.getId())).findAny().get())) //findAny：返回流中的任意元素
                    .collect(Collectors.toList());
            list.addAll(actTaskDTOS);
        }
        return list;
    }

    @Override
    public List<String> formDataShow(String taskID) {
        Task task = taskRuntime.task(taskID);
        /*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2--__!!类型--__!!名称--__!!是否参数--__!!默认值
            例子：
            FormProperty_0lovri0--__!!string--__!!姓名--__!!f--__!!同意!!__--驳回
            FormProperty_1iu6onu--__!!int--__!!年龄--__!!s

            默认值：无、字符常量、FormProperty_开头定义过的控件ID
            是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
            注：类型是可以获取到的，但是为了统一配置原则，都配置到
        */

        //注意!!!!!!!!:表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key“task.getFormKey()”当做任务key
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());

        if (userTask == null) {
            return null;
        }
        List<FormProperty> formProperties = userTask.getFormProperties();
        List<String> collect = formProperties.stream().map(fp -> fp.getId()).collect(Collectors.toList());

        return collect;
    }

    @Override
    public int formDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs) {
        Task task = taskRuntime.task(taskID);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        Boolean hasVariables = false;//没有任何参数
        HashMap<String, Object> variables = new HashMap<String, Object>();
        //前端传来的字符串，拆分成每个控件
        List<ActiFormData> acwfds = new ArrayList<>();
        for (ActWorkflowFormDataDTO awf : awfs) {
            ActiFormData actiFormData = new ActiFormData(processInstance.getBusinessKey(), awf, task);
            acwfds.add(actiFormData);
            //构建参数集合
            if (!"f".equals(awf.getControlIsParam())) {
                variables.put(awf.getControlId(), awf.getControlValue());
                hasVariables = true;
            }
        }//for结束
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        if (hasVariables) {
            //带参数完成任务
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                    .withVariables(variables)
                    .build());
        } else {
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                    .build());
        }

        //写入数据库
        return actWorkflowFormDataService.insertActiFormDatas(acwfds);
    }

    /**
     * <br>方法描述：退回上一步
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/3 16:17
     * <br>备注：无
     */
    @Override
    @Transactional
    public void back(String taskId, String comment, Map<String,Object> variables){
        ActiTodoTaskHistory actiTodoTaskHistory = actiTodoTaskHistoryService.selectActiTodoTaskHistoryByNextTaskId(taskId);
        String instanceId = actiTodoTaskHistory.getProcessInstanceId();
        String taskNodeId = actiTodoTaskHistory.getTaskNodeId();
        String receiver = actiTodoTaskHistory.getReceiver();
        ProcessInstance instance = getProcessInstance(instanceId);
        if (instance.isSuspended()) {
            throw new RuntimeException("流程已被挂起!");
        }
        submit(instanceId, taskId, taskNodeId, receiver, comment, variables);
        ActiTodoTaskHistory preActiTodoTaskHistory = actiTodoTaskHistoryService.selectActiTodoTaskHistoryByTaskId(taskId);
        actiTodoTaskHistoryService.setBackTask(preActiTodoTaskHistory.getTaskId());
    }

    /**
     * <br>方法描述：撤回
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/3 16:17
     * <br>备注：无
     */
    @Override
    @Transactional
    public void recall(String taskId) {
        org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task==null) {
            throw new RuntimeException("任务已处理，不可撤回");
        }
        ActiTodoTaskHistory preActiTodoTaskHistory = actiTodoTaskHistoryService.selectActiTodoTaskHistoryByNextTaskId(taskId);
        if (StringUtils.isEmpty(preActiTodoTaskHistory.getPreTaskId())) {
            //如果是第一个节点，则删除流程实例
            ActiTodoTask actiTodoTask = actiTodoTaskService.selectActiTodoTaskByTaskId(taskId);
            //删除流程实例
            runtimeService.deleteProcessInstance(actiTodoTask.getProcessInstanceId(),"撤回");
            historyService.deleteHistoricProcessInstance(actiTodoTask.getProcessInstanceId());
            //删除待办
            actiTodoTaskService.deleteActiTodoTaskByTaskId(actiTodoTask.getTaskId());
            //删除已办
            actiTodoTaskHistoryService.deleteActiTodoTaskHistoryById(preActiTodoTaskHistory.getId());
        }else {
            //如果不是第一个节点
            actiTodoTaskService.deleteActiTodoTaskByTaskId(preActiTodoTaskHistory.getNextTaskId());
            actiTodoTaskHistoryService.setRecallTask(preActiTodoTaskHistory.getTaskId());
            // TODO: 2021/8/18 variables
            submit(preActiTodoTaskHistory.getProcessInstanceId(), preActiTodoTaskHistory.getNextTaskId(), preActiTodoTaskHistory.getTaskNodeId(), preActiTodoTaskHistory.getReceiver(), null, null);
        }
    }

    /**
     * <br>方法描述：获取所有完成节点
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/3 16:17
     * <br>备注：无
     */
    @Override
    public List<ActiTaskConfig> completeNodes(String instanceId) {
        List<ActiTaskConfig> actiTaskConfigList = activitiMapper.getCompleteNodesByInstanceId(instanceId);
        return actiTaskConfigList;
    }

    @Override
    @Transactional
    public void suspended(String instanceId) {
        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        // 获取相关的状态操作
        boolean suspended = processInstance.isSuspended();
        String id = processInstance.getId();
        if(!suspended){
            // 激活--》挂起
            runtimeService.suspendProcessInstanceById(id);
        }
    }

    @Override
    @Transactional
    public void activate(String instanceId) {
        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        // 获取相关的状态操作
        boolean suspended = processInstance.isSuspended();
        String id = processInstance.getId();
        if(suspended){
            // 挂起--》激活
            runtimeService.activateProcessInstanceById(id);
        }
    }

    @Override
    @Transactional
    public void deleteProcessInstance(String instanceId, String deleteReason) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if(processInstance==null){
            runtimeService.deleteProcessInstance(instanceId,deleteReason);
        }else {
            historyService.deleteHistoricProcessInstance(instanceId);
        }
    }

    /**
     * <br>方法描述：重新指派
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/10 10:12
     * <br>备注：无
     */
    @Override
    @Transactional
    public void reassign(String taskId, String assignee) {
        taskService.unclaim(taskId);
        taskService.claim(taskId, assignee);
        actiTodoTaskService.updateReceiverByTaskId(taskId,assignee);
    }

    @Override
    public List<ActiTodoTaskHistory> getTaskTraces(String instanceId) {
        List<ActiTodoTaskHistory> list = activitiMapper.selectTaskTraces(instanceId);
        for (ActiTodoTaskHistory actiTodoTaskHistory : list) {
            String tenantKey = actiTodoTaskHistory.getTenantKey();
            String receiver = actiTodoTaskHistory.getReceiver();
            actiTodoTaskHistory.setReceiverNickName(userService.getNickName(tenantKey, receiver));
        }
        return list;
    }

    /**
     * 查询流程当前节点的下一步节点
     *
     * @param taskId 任务id
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/23 14:21
     */
    public List<FlowNode> findNextTask(String taskId, Map<String, Object> variables) {
        List<FlowNode> nodeList = new ArrayList<>();
        // 取得已提交的任务,即当前节点
        HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        //查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(historicTask.getProcessInstanceId()).singleResult();
        // 查询流程定义
        Process process = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId()).getMainProcess();
        // 当前节点流定义
        FlowNode sourceFlowElement = (FlowNode) process.getFlowElement(historicTask.getTaskDefinitionKey());
        // 找到当前任务的流程变量
        List<HistoricVariableInstance> listVar = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
        iteratorNextNodes(process, sourceFlowElement, nodeList, listVar, variables);
        return nodeList;
    }

    /**
     * 查询流程当前节点的下一步节点。用于流程提示时的提示
     *
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/23 14:21
     */
    private void iteratorNextNodes(Process process, FlowNode sourceFlowElement,
                                   List<FlowNode> nodeList, List<HistoricVariableInstance> listVar, Map<String, Object> variables) {
        List<SequenceFlow> list = sourceFlowElement.getOutgoingFlows();
        for (SequenceFlow sf : list) {
            sourceFlowElement = (FlowNode) process.getFlowElement(sf.getTargetRef());
            if (StringUtils.isNotEmpty(sf.getConditionExpression())) {
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                for (HistoricVariableInstance var : listVar) {
                    context.setVariable(var.getVariableName(), factory.createValueExpression(var.getValue(), var.getValue().getClass()));
                }
                if (variables != null) {
                    for (Map.Entry<String, Object> entry : variables.entrySet()) {
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        context.setVariable(key, factory.createValueExpression(val, val.getClass()));
                    }
                }

                ValueExpression e = factory.createValueExpression(context, sf.getConditionExpression(), boolean.class);
                if ((Boolean) e.getValue(context)) {
                    nodeList.add(sourceFlowElement);
                }
            }else {
                if (sourceFlowElement instanceof UserTask || sourceFlowElement instanceof EndEvent) {
                    nodeList.add(sourceFlowElement);
                } else if (sourceFlowElement instanceof ExclusiveGateway) {
                    iteratorNextNodes(process, sourceFlowElement, nodeList, listVar, variables);
                }
            }
        }
    }

    private FlowElement getNodeByNodeId(Process process, String nodeId){
        FlowElement flowElement = process.getFlowElement(nodeId);
        return flowElement;
    }

}
