package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import com.hhwy.activiti.core.mapper.ActivitiMapper;
import com.hhwy.activiti.core.service.IActiTaskConfigService;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.common.security.service.TokenService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:04
 * <br>备注：无
 */
@RestController
@RequestMapping("/act/taskConfig")
public class ActiTaskConfigController extends BaseController {
    @Autowired
    private IActiTaskConfigService actiTaskConfigService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiMapper activitiMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * <br>接收参数: procDefId
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/14 10:18
     * <br>备注：无
     */
    @GetMapping("/list")
    @PreAuthorize(hasPermi = "act:taskConfig:query")
    public AjaxResult list(String procDefId) {
        String tenantKey = tokenService.getTenantKey();
        List<ActiTaskConfig> list = actiTaskConfigService.selectActiTaskConfigListByProcDefId(procDefId,tenantKey);
        List<ActiTaskConfig> taskConfigListResult = new ArrayList<>();
        //配置列表查询
        BpmnModel model;
        try {
            model = repositoryService.getBpmnModel(procDefId);
        } catch (ActivitiObjectNotFoundException e) {
            return AjaxResult.error("找不到" + procDefId + "对应的流程定义信息");
        }
        String processKey = activitiMapper.getProcessKeyByDefinitionId(procDefId);
        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                if (e.getClass().toString().equals("class org.activiti.bpmn.model.UserTask")) {
                    UserTask userTask = (UserTask) e;
                    String taskNodeId = userTask.getId();
                    String taskNodeName = userTask.getName();
                    ActiTaskConfig actiTaskConfigNew = new ActiTaskConfig();
                    actiTaskConfigNew.setTaskNodeId(taskNodeId);
                    actiTaskConfigNew.setTaskNodeName(taskNodeName);
                    actiTaskConfigNew.setProcDefId(procDefId);
                    actiTaskConfigNew.setProcDefKey(processKey);
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (ActiTaskConfig workflowTaskConfig : list) {
                            if (workflowTaskConfig.getTaskNodeId().equalsIgnoreCase(taskNodeId)) {
                                actiTaskConfigNew = workflowTaskConfig;
                                list.remove(workflowTaskConfig);
                                break;
                            }
                        }
                    }else {
                        ActiTaskConfig actiTaskConfig = actiTaskConfigService.selectLatestActiTaskConfigByProcDefKeyAndTaskNodeId(processKey, taskNodeId, tenantKey);
                        if(actiTaskConfig==null && !Constants.MASTER_TENANT_KEY.equals(tenantKey)){
                            actiTaskConfig = actiTaskConfigService.selectLatestActiTaskConfigByProcDefKeyAndTaskNodeId(processKey, taskNodeId, Constants.MASTER_TENANT_KEY);
                        }
                        if (actiTaskConfig != null) {
                            String assigneeType = actiTaskConfig.getAssigneeType();
                            actiTaskConfigNew.setAssigneeType(assigneeType);
                            actiTaskConfigNew.setFormUrl(actiTaskConfig.getFormUrl());
                            actiTaskConfigNew.setSameAssigneesNode(actiTaskConfig.getSameAssigneesNode());
                            String assignees = actiTaskConfig.getAssignees();
                            if ("1".equals(assigneeType)) {//配置类型(1：角色:2：人员)
                                if ("0".equals(tokenService.getTenant().getRoleStatus())) {//角色 是否与主租户同步 0：不同步 1：同步
                                    assignees = null;
                                }
                            }else if ("2".equals(assigneeType)){
                                if ("0".equals(tokenService.getTenant().getUserStatus())) {//用户 是否与主租户同步 0：不同步 1：同步
                                    assignees = null;
                                }
                            }
                            actiTaskConfigNew.setAssignees(assignees);
                            actiTaskConfigNew.setAssigneesScope(actiTaskConfig.getAssigneesScope());
                        }
                    }
                    taskConfigListResult.add(actiTaskConfigNew);
                }
            }
        }
        return getDataTableAjaxResult(taskConfigListResult);
    }

    /**
     * 查询流程配置列表
     */
    @GetMapping("/page")
    @PreAuthorize(hasPermi = "act:taskConfig:query")
    public AjaxResult page(ActiTaskConfig actiTaskConfig) {
        startPage();
        List<ActiTaskConfig> list = actiTaskConfigService.selectActiTaskConfigList(actiTaskConfig);
        return getDataTableAjaxResult(list);
    }

    /**
     * 新增保存流程配置
     */
    @Log(title = "流程配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ResponseBody
    @PreAuthorize(hasPermi = "act:taskConfig:add")
    public AjaxResult addSave(@RequestBody List<ActiTaskConfig> actiTaskConfigList) {
        return toAjax(actiTaskConfigService.batchInsertActiTaskConfig(actiTaskConfigList));
    }

    /**
     * 查看流程配置
     */
    @GetMapping("/{id}")
    @PreAuthorize(hasPermi = "act:taskConfig:query")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        ActiTaskConfig actiTaskConfig = actiTaskConfigService.selectActiTaskConfigById(id);
        return AjaxResult.success(actiTaskConfig);
    }

    /**
     * 修改保存流程配置
     */
    @Log(title = "流程配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize(hasPermi = "act:taskConfig:edit")
    public AjaxResult editSave(@RequestBody List<ActiTaskConfig> actiTaskConfigList) {
        return toAjax(actiTaskConfigService.editSave(actiTaskConfigList));
    }

    /**
     * 删除
     */
    @Log(title = "流程配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize(hasPermi = "act:taskConfig:remove")
    public AjaxResult remove(String ids) {
        return toAjax(actiTaskConfigService.deleteActiTaskConfigByIds(ids));
    }
}
