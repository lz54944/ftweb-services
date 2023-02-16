package com.hhwy.activiti.core.domain.vo;

import java.util.Date;

/**
 * <br>描 述： ActReDeploymentVO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class ActReDeploymentVO {
    private String id;
    private Date deployTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }
}
