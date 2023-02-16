package com.hhwy.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author hhwy
 */
public class CacheConstants {
    /**
     * 令牌自定义标识
     */
    public static final String HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    public final static String EXPIRE_TIME_KEY = "expire_time:";

    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 租户字段
     */
    public static final String DETAILS_TENANT_KEY = "tenantKey";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "authorization";

    public final static String ROLE_PERMISSION_UPDATE_COLLECTION_KEY = "role_permission_update_collection";
}
