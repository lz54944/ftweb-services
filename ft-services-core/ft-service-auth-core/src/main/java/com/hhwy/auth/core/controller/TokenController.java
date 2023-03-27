package com.hhwy.auth.core.controller;

import com.hhwy.auth.core.form.LoginBody;
import com.hhwy.auth.core.service.SysLoginService;
import com.hhwy.common.core.constant.CacheConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.DESUtil;
import com.hhwy.common.core.utils.ServletUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * token 控制
 *
 * @author hhwy
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

     /**
        * <br>方法描述：单点认证 获取token
        * <br>接收参数: tenantKey：租户标识、token：加密串、key：加密key
        * <br>返 回 值:
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/11/2 16:21
        * <br>备注：无
        */
    @PostMapping("sso/validate")
    public R<?> ssoValidate(@RequestBody Map<String,String> params) {
        String tenantKey = params.get(CacheConstants.DETAILS_TENANT_KEY);
        String token = params.get("token");
        String key = params.get("key");
        String username = DESUtil.decrypt(key, token);
        // 用户登录
        LoginUser userInfo = sysLoginService.usernameValidate(tenantKey,username);
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo,tenantKey));
    }

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        String tenantKey = ServletUtils.getHeader(CacheConstants.DETAILS_TENANT_KEY);
        Assert.notNull(tenantKey,"租户key不能为空");
        // 用户登录
        LoginUser userInfo = sysLoginService.login(tenantKey, form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo,tenantKey));
    }

    @PostMapping("passwordValidate")
    public AjaxResult lockScreenPasswordValidate(@RequestBody LoginBody form) {
        String tenantKey = ServletUtils.getHeader(CacheConstants.DETAILS_TENANT_KEY);
        String username = tokenService.getLoginUser().getUsername();
        boolean bool= sysLoginService.passwordValidate(tenantKey, username, form.getPassword());
        if(bool){
            return AjaxResult.success("密码正确");
        }
        return AjaxResult.error("密码错误");
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String username = loginUser.getUsername();
            // 记录用户退出日志
            sysLoginService.logout(username);
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken(),loginUser.getTenantKey());
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }
}
