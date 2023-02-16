package com.hhwy.activiti.core.domain.dto;

import org.activiti.engine.repository.ProcessDefinition;

/**
 * <br>描 述： DefinitionIdDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class DefinitionIdDTO {
    private String deploymentID;
    private String resourceName;

    public String getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(String deploymentID) {
        this.deploymentID = deploymentID;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public DefinitionIdDTO() {
    }

    public DefinitionIdDTO(ProcessDefinition processDefinition) {
        this.deploymentID = processDefinition.getDeploymentId();
        this.resourceName = processDefinition.getResourceName();
    }
}
