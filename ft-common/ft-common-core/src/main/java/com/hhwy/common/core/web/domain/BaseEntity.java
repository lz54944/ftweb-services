package com.hhwy.common.core.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity基类
 *
 * @author hhwy
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新着者id
     */
    private Long updateUserId;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /** 租户标识 */
    private String tenantKey;

    /** 部门id */
    private Long deptId;

    /** 部门名称 */
    private String deptName;

    private String ptVar1;

    private String ptVar2;

    private String ptVar3;

    private String ptVar4;

    private String ptVar5;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getPtVar1() {
        return ptVar1;
    }

    public void setPtVar1(String ptVar1) {
        this.ptVar1 = ptVar1;
    }

    public String getPtVar2() {
        return ptVar2;
    }

    public void setPtVar2(String ptVar2) {
        this.ptVar2 = ptVar2;
    }

    public String getPtVar3() {
        return ptVar3;
    }

    public void setPtVar3(String ptVar3) {
        this.ptVar3 = ptVar3;
    }

    public String getPtVar4() {
        return ptVar4;
    }

    public void setPtVar4(String ptVar4) {
        this.ptVar4 = ptVar4;
    }

    public String getPtVar5() {
        return ptVar5;
    }

    public void setPtVar5(String ptVar5) {
        this.ptVar5 = ptVar5;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
