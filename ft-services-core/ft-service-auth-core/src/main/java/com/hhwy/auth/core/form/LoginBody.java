package com.hhwy.auth.core.form;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author hhwy
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码缓存
     */
    private String redisKey;

    /**
     * 验证码
     */
    private String verificationCode;
}
