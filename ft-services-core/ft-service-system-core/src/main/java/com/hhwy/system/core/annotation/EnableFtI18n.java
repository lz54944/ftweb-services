package com.hhwy.system.core.annotation;

import com.hhwy.system.core.config.I18nConfig;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * <br>描 述： EnableFtI18n
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/9/24 17:08
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(I18nConfig.class)
public @interface EnableFtI18n {
}
