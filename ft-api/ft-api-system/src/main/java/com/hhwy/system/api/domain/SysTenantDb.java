package com.hhwy.system.api.domain;

import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 【请填写功能名称】对象 sys_tenant_db
 * 
 * @author jzq
 * @date 2021-09-29
 */
public class SysTenantDb extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 租户标识 */
    @Excel(name = "租户标识")
    private String tenantKey;

    /** 服务名称 */
    @Excel(name = "服务名称")
    private String serviceName;

    /** 数据库地址 */
    @Excel(name = "数据库地址")
    private String dbHost;

    /** 数据库端口号 */
    @Excel(name = "数据库端口号")
    private Integer dbPort;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dbName;

    /** 数据库用户名 */
    @Excel(name = "数据库用户名")
    private String dbUserName;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String dbType;

    /** 数据库密码 */
    @Excel(name = "数据库密码")
    private String dbPassword;

    /** 数据库url */
    @Excel(name = "数据库url")
    private String dbUrl;


    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setTenantKey(String tenantKey){
        this.tenantKey = tenantKey;
    }

    public String getTenantKey(){
        return tenantKey;
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }

    public String getServiceName(){
        return serviceName;
    }

    public void setDbHost(String dbHost){
        this.dbHost = dbHost;
    }

    public String getDbHost(){
        return dbHost;
    }

    public void setDbPort(Integer dbPort){
        this.dbPort = dbPort;
    }

    public Integer getDbPort(){
        return dbPort;
    }

    public void setDbName(String dbName){
        this.dbName = dbName;
    }

    public String getDbName(){
        return dbName;
    }

    public void setDbUserName(String dbUserName){
        this.dbUserName = dbUserName;
    }

    public String getDbUserName(){
        return dbUserName;
    }

    public void setDbType(String dbType){
        this.dbType = dbType;
    }

    public String getDbType(){
        return dbType;
    }

    public void setDbPassword(String dbPassword){
        this.dbPassword = dbPassword;
    }

    public String getDbPassword(){
        return dbPassword;
    }

    public void setDbUrl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    public String getDbUrl(){
        return dbUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tenantKey", getTenantKey())
            .append("serviceName", getServiceName())
            .append("dbHost", getDbHost())
            .append("dbPort", getDbPort())
            .append("dbName", getDbName())
            .append("dbUserName", getDbUserName())
            .append("dbType", getDbType())
            .append("dbPassword", getDbPassword())
            .append("dbUrl", getDbUrl())
            .toString();
    }
}
