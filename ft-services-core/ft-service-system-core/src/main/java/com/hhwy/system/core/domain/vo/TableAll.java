package com.hhwy.system.core.domain.vo;

import lombok.Data;

@Data
public class TableAll {
    //数据库名
    private String tableSchema;
    //表名
    private String tableName;
    //字段名
    private String columnName;
    //顺序号
    private Long  ordinalPosition;
    //字段类型
    private String dataType;
    //备注
    private String columnComment;
}
