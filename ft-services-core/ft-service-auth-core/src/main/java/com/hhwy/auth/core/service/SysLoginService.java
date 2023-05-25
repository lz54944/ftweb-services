package com.hhwy.auth.core.service;

import com.hhwy.auth.core.util.RsaUtils;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.enums.UserStatus;
import com.hhwy.common.core.exception.BaseException;
import com.hhwy.common.core.exception.CaptchaException;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.redis.service.RedisService;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.RemoteLogService;
import com.hhwy.system.api.RemoteUserService;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 *
 * @author hhwy
 */
@Component
public class SysLoginService {
    @Autowired
    private RemoteLogService remoteLogService;

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    public LoginUser usernameValidate(String tenantKey, String username){
        // 租户标识为空 错误
        if (StringUtils.isBlank(tenantKey)) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "租户标识不能为空");
            throw new BaseException("租户标识不能为空");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(tenantKey, username);

        if (R.FAIL == userResult.getCode() || StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new BaseException("用户‘" + username + "’不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }
        remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    /**
     * 登录
     */
    public LoginUser login(String tenantKey, String username, String password, String redisKey, String verificationCode) {
        password = RsaUtils.aesDecryptForFront(password,RsaUtils.KEY_DES);
        // 租户标识为空 错误
        if (StringUtils.isBlank(tenantKey)) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "租户标识不能为空");
            throw new BaseException("租户标识不能为空");
        }
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new BaseException("用户/密码必须填写");
        }
        //校验验证码
        this.checkCaptcha(verificationCode,redisKey);
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new BaseException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new BaseException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(tenantKey, username);

        if (R.FAIL == userResult.getCode()) {
            throw new BaseException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new BaseException("登录用户：" + username + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");

            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_FAIL, "用户密码错误");
            throw new BaseException("用户不存在/密码错误");
        }
        remoteLogService.saveLogininfor(tenantKey, username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    /**
     * 校验验证码
     * @param verificationCode 验证码
     * @param redisKey
     * @throws CaptchaException
     */
    public void checkCaptcha(String verificationCode, String redisKey) throws CaptchaException {
        if (StringUtils.isBlank(verificationCode)){
            throw new CaptchaException("登陆验证码不得为空!");
        }

        if (StringUtils.isBlank(redisKey) || null == redisService.getCacheObject(redisKey)){
            throw new CaptchaException("验证码已过期!");
        }

        //redis中的验证码
        String cache = redisService.getCacheObject(redisKey).toString();
        redisService.deleteObject(redisKey);
        if (!verificationCode.equalsIgnoreCase(cache)){
            throw new CaptchaException("验证码错误!");
        }
    }

    public void logout(String loginName) {
        remoteLogService.saveLogininfor(tokenService.getTenantKey(), loginName, Constants.LOGOUT, "退出成功");
    }

    public boolean passwordValidate(String tenantKey, String username, String password){
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(tenantKey, username);
        SysUser user = userResult.getData().getSysUser();
        return SecurityUtils.matchesPassword(password, user.getPassword());
    }
}