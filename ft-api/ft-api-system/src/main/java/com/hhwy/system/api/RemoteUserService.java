package com.hhwy.system.api;

import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.api.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.factory.RemoteUserFallbackFactory;
import com.hhwy.system.api.model.LoginUser;
import java.util.List;

/**
 * 用户服务
 *
 * @author hhwy
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping(value = "/user/info/{tenantKey}/{username}")
    R<LoginUser> getUserInfo(@PathVariable("tenantKey") String tenantKey, @PathVariable("username") String username);

    /**
     * 通过角色id查询用户信息
     *
     * @param roleIds 角色id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByRoleIds/{roleIds}")
    R<List<SysUser>> getUserListByRoleIds(@PathVariable("roleIds") String roleIds);

    /**
     * 通过master中的角色id查询用户信息
     *
     * @param originalRoleIds 角色id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByOriginalRoleIds/{originalRoleIds}")
    R<List<SysUser>> getUserListByOriginalRoleIds(@PathVariable("originalRoleIds") String originalRoleIds);

    /**
     * 通过角色id和部门id查询用户信息
     *
     * @param roleIds 角色id 多个用逗号隔开
     * @param deptIds 部门id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByRoleIdsAndDeptIds/{roleIds}/{deptIds}")
    R<List<SysUser>> getUserListByRoleIdsAndDeptIds(@PathVariable("roleIds") String roleIds, @PathVariable("deptIds") String deptIds);

    /**
     * 通过master中的角色id和master中的部门id查询用户信息
     *
     * @param originalRoleIds 角色id 多个用逗号隔开
     * @param deptIds 部门id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByOriginalRoleIdsAndDeptIds/{originalRoleIds}/{deptIds}")
    R<List<SysUser>> getUserListByOriginalRoleIdsAndDeptIds(@PathVariable("originalRoleIds") String originalRoleIds, @PathVariable("deptIds") String deptIds);

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping(value = "/user/info/{username}")
    R<SysUser> getUserInfoByUsername(@PathVariable("username") String username);

    /**
     * 通过用户名查询用户信息
     *
     * @param usernames 用户名 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByUsernames/{usernames}")
    R<List<SysUser>> getUserListByUsernames(@PathVariable("usernames") String usernames);

    /**
     * 通过用户id查询用户信息
     *
     * @param userId 用户id
     * @return 结果
     */
    @GetMapping(value = "/user/{userId}")
    R<List<SysUser>> getUserInfoByUserId(@PathVariable("userId") Long userId);

    /**
     * 通过用户id查询用户信息
     *
     * @param userIds 用户id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByUserIds/{userIds}")
    R<List<SysUser>> getUserListByUserIds(@PathVariable("userIds") String userIds);

    /**
     * 通过master中的用户id查询用户信息
     *
     * @param originalUserIds 用户id 多个用逗号隔开
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByOriginalUserIds/{originalUserIds}")
    R<List<SysUser>> getUserListByOriginalUserIds(@PathVariable("originalUserIds") String originalUserIds);

    /**
     * 通过用户昵称 模糊查询用户信息
     *
     * @return 结果
     */
    @GetMapping(value = "/user/selectUserListByNickName/{nickName}")
    R<List<SysUser>> getUserListByNickName(@PathVariable("nickName") String nickName);

    @GetMapping("/user/getUserAllList")
    AjaxResult selectUserAllList(@PathVariable("deptId")Long deptId);

}
