package com.hhwy.system.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门关联流程图查询参数
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncFlowQueryVo {

    /** 功能模块 */
    private String funModel;

    /** 部门id */
    private Long deptId;

    /** 租户标识 */
    private String tenantKey;
}
