package com.hhwy.system.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 业务流程单子对象 sys_business_form_item
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Data
public class SysBusinessFormItem extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 唯一标识主键ID */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 字段编码 */
    @Excel(name = "字段编码")
    private String colCode;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String colName;

    /** 字段类型 */
    @Excel(name = "字段类型")
    private String colType;

    /** 字典编码 */
    @Excel(name = "字典编码")
    private String dictCode;

    /** 条件字段别名 */
    @Excel(name = "条件字段别名")
    private String dbWhereParam;

    /** 查询sql */
    @Excel(name = "查询sql")
    private String querySql;

    /** sql参数 */
    @Excel(name = "sql参数")
    private String sqlParams;

    /** 顺序号 */
    @Excel(name = "顺序号")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderno;

    /** 是否展示 */
    @Excel(name = "是否展示")
    private String isshow;

    /** 是否可编辑 */
    @Excel(name = "是否可编辑")
    private String isedit;

    /** 是否附件 */
    @Excel(name = "是否附件")
    private String isfile;

    /** 字段备注 */
    @Excel(name = "字段备注")
    private String colComm;

    /** 附件组 */
    @Excel(name = "附件组")
    private String fileGroupId;

    /** 删除标识:0:未删除;1:已删除; */
    private String delFlag;

    /** 创建人机构ID */
    @Excel(name = "创建人机构ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long createOrgId;

    /** 修改人机构ID */
    @Excel(name = "修改人机构ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long updateOrgId;

    /** 填报人 */
    @Excel(name = "填报人")
    private String createUser;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updateUser;
    /**
     * 创建人id
     */
    private  String createBy;
    /**
     * 修改人id
     */
    private String updateBy;
    /**
     * 流程业务id
     */
    private String flowFormId;

}
