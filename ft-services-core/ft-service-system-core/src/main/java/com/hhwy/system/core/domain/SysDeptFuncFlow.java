package com.hhwy.system.core.domain;

import com.hhwy.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 部门关联流程图对象 ipm_sys_dept_func_flow
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDeptFuncFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 唯一主键 */
    private Long id;

    /** 功能模块 */
    @ApiModelProperty("功能模块")
    private String funModel;

    /** 流程标识编码 */
    @ApiModelProperty("流程标识编码")
    private String actCode;

    /** 流程名称 */
    @ApiModelProperty("流程名称")
    private String actName;

    /** 部门id */
    @ApiModelProperty("部门id")
    private Long deptId;

    /** 部门名称 */
    @ApiModelProperty("部门名称")
    private String deptName;

    /** 创建者部门id */
    @ApiModelProperty("创建者部门id")
    private Long createDeptId;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    /** 创建人名称 */
    @ApiModelProperty("创建人名称")
    private String createUser;

    /** 修改人ID */
    @ApiModelProperty("修改人ID")
    private String updateUser;

    /** 删除标识  0:未删除;1:已删除; */
    @ApiModelProperty("删除标识")
    private String delFlag;

    /** 租户标识 */
    @ApiModelProperty("租户标识")
    private String tenantKey;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("funModel", getFunModel())
            .append("actCode", getActCode())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("createDeptId", getCreateDeptId())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createUser", getCreateUser())
            .append("updateTime", getUpdateTime())
            .append("updateUser", getUpdateUser())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
