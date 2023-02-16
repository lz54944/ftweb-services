package com.hhwy.system.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;

/**
 * 租户-资源映射关系对象 sys_tenant_resource
 * 
 * @author jzq
 * @date 2021-08-11
 */
public class SysTenantResource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 资源主键 */
    @Excel(name = "资源主键")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long resourceId;

    /** 资源所在表名 */
    @Excel(name = "资源所在表名")
    private String resourceTable;

    /** 租户标识 */
    @Excel(name = "租户标识")
    private String tenantKey;

    /**
     * 用于树形控件的层级独立或者层级关联
     * 半选状态(自身没有被选中，选中的是子集菜单)
     * 0：自身被选中  1：自身未被选中，选中的是子集菜单
     */
    private String mistyFlag;

    /** 对应master中的资源主键 */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long resourceOriginalId;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setResourceId(Long resourceId){
        this.resourceId = resourceId;
    }

    public Long getResourceId(){
        return resourceId;
    }

    public void setResourceTable(String resourceTable){
        this.resourceTable = resourceTable;
    }

    public String getResourceTable(){
        return resourceTable;
    }

    public void setTenantKey(String tenantKey){
        this.tenantKey = tenantKey;
    }

    public String getTenantKey(){
        return tenantKey;
    }

    public String getMistyFlag() {
        return mistyFlag;
    }

    public void setMistyFlag(String mistyFlag) {
        this.mistyFlag = mistyFlag;
    }

    public Long getResourceOriginalId() {
        return resourceOriginalId;
    }

    public void setResourceOriginalId(Long resourceOriginalId) {
        this.resourceOriginalId = resourceOriginalId;
    }
}
