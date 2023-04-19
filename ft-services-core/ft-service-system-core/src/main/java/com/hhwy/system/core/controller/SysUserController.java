package com.hhwy.system.core.controller;

import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.poi.ExcelUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.api.model.LoginUser;
import com.hhwy.system.core.service.ISysPermissionService;
import com.hhwy.system.core.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户信息
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/list")
    public AjaxResult list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTableAjaxResult(list);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/list/all")
    public AjaxResult listAll(SysUser user) {
        List<SysUser> list = userService.selectUserListAll(user);
        return AjaxResult.success(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) throws IOException {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtils<SysUser> util = new ExcelUtils<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize(hasPermi = "system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtils<SysUser> util = new ExcelUtils<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUserName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    @PreAuthorize(hasPermi = "system:user:import")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils<SysUser> util = new ExcelUtils<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 获取当前用户信息
     */
    //@PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/info/{tenantKey}/{username}")
    public R<LoginUser> info(@PathVariable("tenantKey") String tenantKey, @PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(tenantKey, username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/info/{username}")
    public R<SysUser> getUserInfoByUsername(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(SecurityUtils.getTenantKey(), username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名错误");
        }
        return R.ok(sysUser);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/selectUserListByUsernames/{usernames}")
    public R<List<SysUser>> selectUserListByUsernames(@PathVariable("usernames") String usernames) {
        List<SysUser> sysUsers = userService.selectUserListByUsernames(SecurityUtils.getTenantKey(), usernames);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/selectUserListByRoleIds/{roleIds}")
    public R<List<SysUser>> selectUserListByRoleIds(@PathVariable("roleIds") String roleIds) {
        List<SysUser> sysUsers = userService.selectUserListByRoleIds(roleIds);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/selectUserListByOriginalRoleIds/{originalRoleIds}")
    R<List<SysUser>> selectUserListByOriginalRoleIds(@PathVariable("originalRoleIds") String originalRoleIds) {
        List<SysUser> sysUsers = userService.selectUserListByOriginalRoleIds(originalRoleIds);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("/selectUserListByRoleIdsAndDeptIds/{roleIds}/{deptIds}")
    public R<List<SysUser>> selectUserListByRoleIdsAndDeptIds(@PathVariable("roleIds") String roleIds, @PathVariable("deptIds") String deptIds) {
        List<SysUser> sysUsers = userService.selectUserListByRoleIdsAndDeptIds(roleIds, deptIds);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/selectUserListByOriginalRoleIdsAndDeptIds/{originalRoleIds}/{deptIds}")
    R<List<SysUser>> selectUserListByOriginalRoleIdsAndDeptIds(@PathVariable("originalRoleIds") String originalRoleIds, @PathVariable("deptIds") String deptIds) {
        List<SysUser> sysUsers = userService.selectUserListByOriginalRoleIdsAndDeptIds(originalRoleIds, deptIds);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/selectUserListByUserIds/{userIds}")
    R<List<SysUser>> selectUserListByUserIds(@PathVariable("userIds") String userIds) {
        List<SysUser> sysUsers = userService.selectUserListByUserIds(userIds);
        return R.ok(sysUsers);
    }

    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/selectUserListByOriginalUserIds/{originalUserIds}")
    R<List<SysUser>> selectUserListByOriginalUserIds(@PathVariable("originalUserIds") String originalUserIds) {
        List<SysUser> sysUsers = userService.selectUserListByOriginalUserIds(originalUserIds);
        return R.ok(sysUsers);
    }

    /**
     * 通过用户昵称 模糊查询用户信息
     *
     * @return 结果
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/selectUserListByNickName/{nickName}")
    R<List<SysUser>> selectUserListByNickName(@PathVariable("nickName") String nickName) {
        List<SysUser> sysUsers = userService.selectUserListByNickName(nickName);
        return R.ok(sysUsers);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    //@PreAuthorize(hasPermi = "system:user:query")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser sysUser = tokenService.getSysUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userService.selectUserById(sysUser.getUserId()));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 查询用户列表（包括岗位，部门，监管部门）
     * @param deptId
     * @return
     */
    @GetMapping("getUserAllList")
    public AjaxResult selectUserAllList(@PathVariable("deptId")Long deptId) {
        SysUser user = new SysUser();
        user.setDeptId(deptId);
        startPage();
        List<SysUser> list = userService.selectUserAllList(user);
        return getDataTableAjaxResult(list);
    }

    /**
     * 新增代码
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping("getLoginUser")
    public AjaxResult getLoginUser() {
        Map<String,Object> result = new ConcurrentHashMap<>();

        SysUser sysUser = tokenService.getSysUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);

        result.put("user", userService.selectUserById(sysUser.getUserId()));
        result.put("roles", roles);
        result.put("permissions", permissions);
        result.put("userId",String.valueOf(sysUser.getUserId()));
        result.put("userName",sysUser.getUserName());
        result.put("nickName",sysUser.getNickName());
        result.put("phoneNumber",sysUser.getPhoneNumber());
        result.put("sex",sysUser.getSex());
        result.put("deptId",String.valueOf(sysUser.getDept().getDeptId()));
        result.put("deptName",sysUser.getDept().getDeptName());

        return AjaxResult.success(result);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        SysUser sysUser = userService.selectUserById(userId);
        return AjaxResult.success(sysUser);
    }

    /**
     * 新增用户
     */
    @PreAuthorize(hasPermi = "system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateUser(SecurityUtils.getUserName());
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateUser(SecurityUtils.getUserName());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize(hasPermi = "system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateUser(SecurityUtils.getUserName());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateUser(SecurityUtils.getUserName());
        return toAjax(userService.updateUserStatus(user));
    }

}
