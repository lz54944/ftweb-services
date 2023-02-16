package com.hhwy.system.core.controller;

import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.ServletUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.file.ImageUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.api.model.LoginUser;
import com.hhwy.system.core.service.ISysPermissionService;
import com.hhwy.system.core.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * 个人信息 业务处理
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        /*String username = SecurityUtils.getUserName();
        String tenantKey = SecurityUtils.getTenantKey();
        SysUser sysUser = userService.selectUserByUserName(tenantKey,username);*/
        SysUser sysUser = tokenService.getSysUser();
        return AjaxResult.success(sysUser);
    }

    /**
     * 获取当前用户权限信息
     */
    @GetMapping("/permissions")
    public AjaxResult permissions() {
        Set<String> permissions = permissionService.selectButtonPermissionsByUserId(tokenService.getSysUser());
        return AjaxResult.success(permissions);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(user) > 0) {
            LoginUser loginUser = tokenService.getLoginUser();
            // 更新缓存用户信息
            loginUser.getSysUser().setNickName(user.getNickName());
            loginUser.getSysUser().setPhoneNumber(user.getPhoneNumber());
            loginUser.getSysUser().setEmail(user.getEmail());
            loginUser.getSysUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody Map<String,String> pwdMap) {
        String oldPassword = pwdMap.get("oldPassword");
        String newPassword = pwdMap.get("newPassword");
        String username = SecurityUtils.getUserName();
        String tenantKey = SecurityUtils.getTenantKey();
        SysUser user = userService.selectUserByUserName(tenantKey,username);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(username, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            LoginUser loginUser = tokenService.getLoginUser();
            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarFile") MultipartFile file){
        try {
            if (!file.isEmpty()) {
                InputStream inputStream = file.getInputStream();
                String contentType = file.getContentType();
                String imgBase64Str = "data:"+contentType+";base64,"+ImageUtils.getImgBase64Str(inputStream);
                LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
                if (userService.updateUserAvatar(loginUser.getUsername(), imgBase64Str)) {
                    AjaxResult ajax = AjaxResult.success();
                    ajax.put("imgBase64", imgBase64Str);
                    // 更新缓存用户头像
                    loginUser.getSysUser().setAvatar(imgBase64Str);
                    tokenService.setLoginUser(loginUser);
                    return ajax;
                }
            }
            return AjaxResult.error("上传图片异常，请联系管理员");
        }catch (Exception e){
            return AjaxResult.error("上传图片异常，请联系管理员");
        }
    }
}
