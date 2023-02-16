package com.hhwy.activiti.core.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * acti_todo_task
 *
 * @author jzq
 * @date 2021-07-20
 */
public class ActiTodoTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 业务表id
     */
    @Excel(name = "业务表id")
    private String businessId;

    /**
     * 接收人  对应userName
     */
    @Excel(name = "接收人")
    private String receiver;

    /**
     * 发送人  对应nickName
     * 实时联查 不存数据库
     */
    private String receiverNickName;

    /**
     * 发送人  对应userName
     */
    @Excel(name = "发送人")
    private String sender;

    /**
     * 发送人  对应nickName
     * 实时联查 不存数据库
     */
    private String senderNickName;

    /**
     * 流程实例ID
     */
    @Excel(name = "流程实例ID")
    private String processInstanceId;

    /**
     * 流程定义key
     */
    @Excel(name = "流程定义key")
    private String processKey;

    /**
     * 流程名称
     */
    @Excel(name = "流程名称")
    private String processName;

    /**
     * 任务id 对应 act_hi_taskinst ID_
     */
    @Excel(name = "任务id")
    private String taskId;

    /**
     * 任务节点ID
     */
    @Excel(name = "任务节点ID")
    private String taskNodeId;

    /**
     * 任务节点名称
     */
    @Excel(name = "任务节点名称")
    private String taskNodeName;

    /**
     * 状态：0:正常,1:挂起
     */
    @Excel(name = "状态：0:正常,1:挂起")
    private int taskState;

    /**
     * 操作日期限制(超过该日期禁止操作)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作日期限制(超过该日期禁止操作)", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date timeLimit;

    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date taskStartTime;

    /**
     * 表单地址
     */
    @Excel(name = "表单地址")
    private String formUrl;

    /**
     * 存放其他数据
     */
    @Excel(name = "存放其他数据")
    private String variables;

    /**
     * 上一个任务id 对应 act_hi_taskinst ID_
     */
    @Excel(name = "上一个任务id")
    private String preTaskId;

    /**
     * 上一个任务节点ID
     */
    @Excel(name = "上一个任务节点ID")
    private String preTaskNodeId;

    /**
     * 上一个任务节点名称
     */
    @Excel(name = "上一个任务节点名称")
    private String preTaskNodeName;

    private String tenantKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskNodeId() {
        return taskNodeId;
    }

    public void setTaskNodeId(String taskNodeId) {
        this.taskNodeId = taskNodeId;
    }

    public String getTaskNodeName() {
        return taskNodeName;
    }

    public void setTaskNodeName(String taskNodeName) {
        this.taskNodeName = taskNodeName;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public Date getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Date timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getPreTaskId() {
        return preTaskId;
    }

    public void setPreTaskId(String preTaskId) {
        this.preTaskId = preTaskId;
    }

    public String getPreTaskNodeId() {
        return preTaskNodeId;
    }

    public void setPreTaskNodeId(String preTaskNodeId) {
        this.preTaskNodeId = preTaskNodeId;
    }

    public String getPreTaskNodeName() {
        return preTaskNodeName;
    }

    public void setPreTaskNodeName(String preTaskNodeName) {
        this.preTaskNodeName = preTaskNodeName;
    }

    public String getReceiverNickName() {
        return receiverNickName;
    }

    public void setReceiverNickName(String receiverNickName) {
        this.receiverNickName = receiverNickName;
    }

    public String getSenderNickName() {
        return senderNickName;
    }

    public void setSenderNickName(String senderNickName) {
        this.senderNickName = senderNickName;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }
}
