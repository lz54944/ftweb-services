package com.hhwy.activiti.api.domain;

import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 流程-业务对象 acti_business_info
 * 
 * @author jzq
 * @date 2021-08-24
 */
public class ActiBusinessInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 业务主键 */
    @Excel(name = "业务主键")
    private String businessId;

    /** 业务表名称 */
    @Excel(name = "业务表名称")
    private String businessTableName;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;

    /** 当前节点Id */
    @Excel(name = "当前节点Id")
    private String processNodeId;

    /** 当前任务Id */
    @Excel(name = "当前任务Id")
    private String processTaskId;

    /** 当前任务名 */
    @Excel(name = "当前任务名")
    private String processTaskName;

    /** 当前任务处理人 */
    @Excel(name = "当前任务处理人")
    private String processTaskMan;

    private String tenantKey;

    /** 表单地址 */
    private String formUrl;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setBusinessId(String businessId){
        this.businessId = businessId;
    }

    public String getBusinessId(){
        return businessId;
    }

    public void setBusinessTableName(String businessTableName){
        this.businessTableName = businessTableName;
    }

    public String getBusinessTableName(){
        return businessTableName;
    }

    public void setProcessInstanceId(String processInstanceId){
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId(){
        return processInstanceId;
    }

    public void setProcessNodeId(String processNodeId){
        this.processNodeId = processNodeId;
    }

    public String getProcessNodeId(){
        return processNodeId;
    }

    public void setProcessTaskId(String processTaskId){
        this.processTaskId = processTaskId;
    }

    public String getProcessTaskId(){
        return processTaskId;
    }

    public void setProcessTaskName(String processTaskName){
        this.processTaskName = processTaskName;
    }

    public String getProcessTaskName(){
        return processTaskName;
    }

    public void setProcessTaskMan(String processTaskMan){
        this.processTaskMan = processTaskMan;
    }

    public String getProcessTaskMan(){
        return processTaskMan;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }
}
