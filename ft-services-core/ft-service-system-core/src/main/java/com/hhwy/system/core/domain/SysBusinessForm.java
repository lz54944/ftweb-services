package com.hhwy.system.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 业务流程单对象 sys_business_form
 *
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Data
public class SysBusinessForm extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 业务流程表单id
     */
    @Excel(name = "业务流程表单id")
    private String flowFormId;

    /**
     * 功能模块
     */
    @Excel(name = "功能模块")
    private String module;

    /**
     * 数据库表
     */
    @Excel(name = "数据库表")
    private String tableDbCode;

    /**
     * 表单编码
     */
    @Excel(name = "表单编码")
    private String formCode;

    /**
     * 表描述
     */
    @Excel(name = "表描述")
    private String tableName;

    /**
     *  流程表单地址
     */
    @Excel(name = "流程表单地址")
    private String bpmnFormUrl;

    /**
     * sql参数
     */
    @Excel(name = "sql参数")
    private String sqlParams;

    /**
     * 查询sql
     */
    @Excel(name = "查询sql")
    private String querySql;

    /**
     * 删除标识:0:未删除;1:已删除;
     */
    private String delFlag;

    /**
     * 创建人机构id
     */
    @Excel(name = "创建人机构id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createOrgId;

    /**
     * 修改人机构id
     */
    @Excel(name = "修改人机构id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateOrgId;

    /**
     * 填报人
     */
    @Excel(name = "填报人")
    private String createUser;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private String updateUser;

    /**
     * 创建人id
     */
    private  String createBy;
    /**
     * 修改人id
     */
    private  String updateBy;

    private  String tableNameEr;
    /**
     * 子表
     */
    private List<SysBusinessFormItem> sysBusinessFormItemList;

}
