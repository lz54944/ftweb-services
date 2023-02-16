package com.hhwy.common.core.utils.token;

import com.hhwy.common.core.constant.CacheConstants;

public class TokenUtils {

    public static String getTokenKey(String token,String tenantKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + tenantKey + ":" + token;
    }

    public static String getTokenKey(String tenantKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + tenantKey + ":" + "*";
    }

    public static String getTokenKey() {
        return CacheConstants.LOGIN_TOKEN_KEY + "*";
    }

}
