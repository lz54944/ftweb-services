package com.hhwy.activiti.core.domain.dto;

import com.hhwy.common.core.web.domain.BaseEntity;

/**
 * <br>描 述： ActWorkflowFormDataDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class ActWorkflowFormDataDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 表单id
     */
    private String controlId;
    private String controlType;


    /**
     * 表单名称
     */
    private String controlLable;

    private String controlIsParam;

    /**
     * 表单值
     */
    private String controlValue;
    private String controlDefault;


    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlValue(String controlValue) {
        this.controlValue = controlValue;
    }

    public String getControlValue() {
        return controlValue;
    }


    public String getControlIsParam() {
        return controlIsParam;
    }

    public void setControlIsParam(String controlIsParam) {
        this.controlIsParam = controlIsParam;
    }

    public String getControlLable() {
        return controlLable;
    }

    public void setControlLable(String controlLable) {
        this.controlLable = controlLable;
    }

    public String getControlDefault() {
        return controlDefault;
    }

    public void setControlDefault(String controlDefault) {
        this.controlDefault = controlDefault;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }
}
