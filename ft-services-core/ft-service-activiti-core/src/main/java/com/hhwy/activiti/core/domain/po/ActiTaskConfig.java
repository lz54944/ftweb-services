package com.hhwy.activiti.core.domain.po;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;

import java.util.Objects;

/**
 * 流程配置
 *
 * @author jzq
 * @date 2021-07-13
 */
public class ActiTaskConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 模型定义id  对应表act_re_procdef的id_
     */
    @Excel(name = "模型定义id  对应表act_re_procdef的id_")
    private String procDefId;

    /**
     * 模型key 对应表act_re_procdef的key_
     */
    @Excel(name = "模型key 对应表act_re_procdef的key_")
    private String procDefKey;

    /**
     * 任务节点id
     */
    @Excel(name = "任务节点id")
    private String taskNodeId;

    /**
     * 任务节点名称
     */
    @Excel(name = "任务节点名称")
    private String taskNodeName;

    /**
     * 配置类型(1：角色:2：人员)
     */
    @Excel(name = "配置类型(1：角色:2：人员)")
    private String assigneeType;

    /**
     * 任务参与者
     */
    @Excel(name = "任务参与者")
    private String assignees;

    /**
     * 相同节点处理人
     */
    @Excel(name = "相同节点处理人")
    private String sameAssigneesNode;

    /**
     * 处理人范围（1：全部  2：本部门）
     */
    @Excel(name = "处理人范围", readConverterExp = "1=：全部,2=：本部门")
    private String assigneesScope;

    /**
     * 表单地址
     */
    @Excel(name = "表单地址")
    private String formUrl;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private String createUser;

    /**
     * 更新者
     */
    @Excel(name = "更新者")
    private String updateUser;

    /**
     * 是否是结束节点
     */
    private boolean isEnd;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setTaskNodeId(String taskNodeId) {
        this.taskNodeId = taskNodeId;
    }

    public String getTaskNodeId() {
        return taskNodeId;
    }

    public void setTaskNodeName(String taskNodeName) {
        this.taskNodeName = taskNodeName;
    }

    public String getTaskNodeName() {
        return taskNodeName;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees;
    }

    public String getAssignees() {
        return assignees;
    }

    public void setSameAssigneesNode(String sameAssigneesNode) {
        this.sameAssigneesNode = sameAssigneesNode;
    }

    public String getSameAssigneesNode() {
        return sameAssigneesNode;
    }

    public void setAssigneesScope(String assigneesScope) {
        this.assigneesScope = assigneesScope;
    }

    public String getAssigneesScope() {
        return assigneesScope;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "ActiTaskConfig{" +
                "id=" + id +
                ", procDefId='" + procDefId + '\'' +
                ", procDefKey='" + procDefKey + '\'' +
                ", taskNodeId='" + taskNodeId + '\'' +
                ", taskNodeName='" + taskNodeName + '\'' +
                ", assigneeType='" + assigneeType + '\'' +
                ", assignees='" + assignees + '\'' +
                ", sameAssigneesNode='" + sameAssigneesNode + '\'' +
                ", assigneesScope='" + assigneesScope + '\'' +
                ", formUrl='" + formUrl + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", isEnd=" + isEnd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiTaskConfig that = (ActiTaskConfig) o;
        return Objects.equals(taskNodeId, that.taskNodeId);
    }

}
