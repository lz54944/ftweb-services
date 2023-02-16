package com.hhwy.system.core.config;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <br>描 述： I18nLocaleChangeInterceptor
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/9/24 17:08
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class I18nLocaleChangeInterceptor extends LocaleChangeInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {
        String newLocale = request.getParameter(getParamName());
        if (newLocale != null) {
            request.getSession().setAttribute("local", newLocale);
        }
        return super.preHandle(request, response, handler);
    }
}
