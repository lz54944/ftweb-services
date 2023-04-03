package com.hhwy.activiti.core.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActReModelVo {

    /** 流程图标识 */
    private String key;

    /** 流程名称 */
    private String name;
}
