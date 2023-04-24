package com.hhwy.file.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhwy.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 文件信息
 *
 * @author hhwy
 */
@Data
public class SysFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 业务id */
    private String businessId;

    /** 业务功能名称(数据库名_表名) */
    private String businessName;

    /** 资源定位地址（doc_id或url） */
    private String fileUrl;

    /** 文件全路径 */
    private String fileFullUrl;

    /** 显示文件名称 */
    private String showFileName;

    /** 实际文件名称 */
    private String actFileName;

    /** 文件大小 */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long fileSize;

    /** 文件类型 */
    private String fileType;

    /**
     * 文件名称（旧字段）
     */
    private String name;

    /**
     * 文件地址（旧字段）
     */
    private String url;
}
