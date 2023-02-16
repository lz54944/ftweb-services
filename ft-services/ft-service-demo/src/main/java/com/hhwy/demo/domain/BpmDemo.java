package com.hhwy.demo.domain;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.common.core.web.domain.BaseEntity;

public class BpmDemo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long id;

    private String number;

    private String title;

    private String content;

    private ActiBusinessInfo actiBusinessInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ActiBusinessInfo getActiBusinessInfo() {
        return actiBusinessInfo;
    }

    public void setActiBusinessInfo(ActiBusinessInfo actiBusinessInfo) {
        this.actiBusinessInfo = actiBusinessInfo;
    }
}
