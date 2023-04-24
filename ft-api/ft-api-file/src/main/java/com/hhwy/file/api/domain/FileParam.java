package com.hhwy.file.api.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName fileParam
 * @Description:
 * @Author syd
 * @Date 2022/2/17
 * @Version V1.0
 **/
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileParam {

    @NotBlank(message = "业务表单id不能为空")
    private String businessId;

    @NotBlank(message = "业务模块名称不能为空")
    private String businessName;

    private String showFileName;

    private String fileType;

    private Long deptId;

    private String deptName;

    private String remark;

    @Builder.Default
    private String whetherGropeRemark = "0";

}
