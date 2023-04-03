package com.hhwy.system.core.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门关联流程图对象 ipm_sys_dept_func_flow
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncFlowVo {
    private static final long serialVersionUID = 1L;

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
}
