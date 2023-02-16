package com.hhwy.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;
import java.util.Date;

/*通用日志*/
public class SysGeneralLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @Excel(name = "日志主键", cellType = Excel.ColumnType.NUMERIC)
    private Long logId;

    /**
     * 日志标题
     */
    @Excel(name = "日志标题")
    private String logTitle;

    /**
     * 日志类型（0其它 1推送 2接收 3拉取）
     */
    @Excel(name = "日志类型", readConverterExp = "0=其它,1=推送,2=接收,3=拉取")
    private Integer logType;

    /**
     * 触发类型（0其它 1自动 2手动）
     */
    @Excel(name = "触发类型", readConverterExp = "0=其它,1=自动,2=手动")
    private Integer triggerType;

    /**
     * 请求方式
     */
    @Excel(name = "请求方式")
    private String requestMethod;

    /**
     * 请求地址
     */
    @Excel(name = "请求地址")
    private String requestUrl;

    /**
     * 请求参数
     */
    @Excel(name = "请求参数,json格式")
    private String requestParam;

    /**
     * 返回结果
     */
    @Excel(name = "返回结果,json格式")
    private String requestResult;

    /**
     * 结果状态（1成功 2失败）
     */
    @Excel(name = "结果状态", readConverterExp = "1=成功,2=失败")
    private Integer resultStatus;

    /**
     * 操作人员
     */
    @Excel(name = "操作人员")
    private String operateUser;

    /**
     * 操作地址
     */
    @Excel(name = "操作地址")
    private String operateIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operateLocation;

    /**
     * 错误消息
     */
    @Excel(name = "错误消息")
    private String errorMsg;

    /**
     * 触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "触发时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date triggerTime;

    /** 备用字段1 */
    private String var1;

    /** 备用字段2 */
    private String var2;

    /** 备用字段3 */
    private String var3;

    /** 备用字段4 */
    private String var4;

    /** 备用字段5 */
    private String var5;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Integer getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }

    public Integer getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getOperateLocation() {
        return operateLocation;
    }

    public void setOperateLocation(String operateLocation) {
        this.operateLocation = operateLocation;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getVar4() {
        return var4;
    }

    public void setVar4(String var4) {
        this.var4 = var4;
    }

    public String getVar5() {
        return var5;
    }

    public void setVar5(String var5) {
        this.var5 = var5;
    }
}
