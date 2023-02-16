package com.hhwy.activiti.core.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 代理对象 acti_task_agent
 * 
 * @author jzq
 * @date 2021-08-31
 */
public class ActiTaskAgent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 代理用户 */
    @Excel(name = "代理用户")
    private String agentUser;

    /** 代理类型，0:代理全部流程 1:仅代理部分流程 */
    @Excel(name = "代理类型，0:代理全部流程 1:仅代理部分流程")
    private String agentType;

    /** 流程key, 仅代理类型为1时有意义 */
    @Excel(name = "流程key, 仅代理类型为1时有意义")
    private String processKey;

    /**
     * 字段描述：流程名称
     */
    private String processName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setAgentUser(String agentUser){
        this.agentUser = agentUser;
    }

    public String getAgentUser(){
        return agentUser;
    }

    public void setAgentType(String agentType){
        this.agentType = agentType;
    }

    public String getAgentType(){
        return agentType;
    }

    public void setProcessKey(String processKey){
        this.processKey = processKey;
    }

    public String getProcessKey(){
        return processKey;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }

    public Date getStartTime(){
        return startTime;
    }

    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }

    public Date getEndTime(){
        return endTime;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}
