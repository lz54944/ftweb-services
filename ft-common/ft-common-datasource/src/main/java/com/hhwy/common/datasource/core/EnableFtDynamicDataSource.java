package com.hhwy.common.datasource.core;

import com.hhwy.common.datasource.config.DataSourceConfig;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * <br>描 述： EnableFtDynamicDataSource
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:21
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataSourceConfig.class)
public @interface EnableFtDynamicDataSource {
}
