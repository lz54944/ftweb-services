package com.hhwy.system.core.mapper;

import com.hhwy.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author hhwy
 */
public interface SysUserMapper {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(@Param("sysUser") SysUser sysUser, @Param("tenantKeyList") List<String> tenantKeyList);

    List<SysUser> selectUserListAll(@Param("sysUser") SysUser sysUser, @Param("tenantKeyList") List<String> tenantKeyList);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(@Param("userName") String userName, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(@Param("userId") Long userId, @Param("tenantKey") String tenantKey);

    List<SysUser> selectUserListByTenantKey(String tenantKey);
    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    int updateUserStatus(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    int realDeleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    int realDeleteUserByIds(List<Long> userIdList);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    int checkUserNameUnique(@Param("userName") String userName, @Param("tenantKeyList")List<String> tenantKeyList);

    int checkUserNameUniqueByTenantKey(@Param("tenantKey") String tenantKey, @Param("userName") String userName);
    /**
     * 校验手机号码是否唯一
     *
     * @param phoneNumber 手机号码
     * @return 结果
     */
    SysUser checkPhoneUnique(@Param("phoneNumber") String phoneNumber, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    SysUser checkEmailUnique(@Param("email") String email, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 根据条件分页查询未已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectAllocatedList(@Param("user") SysUser user, @Param("roleId") Long roleId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUnallocatedList(@Param("user") SysUser user, @Param("roleId") Long roleId, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    List<SysUser> selectUserListByRoleIdList(@Param("roleIdList") List<String> roleIdList, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    List<SysUser> selectUserListByOriginalRoleIdList(@Param("originalRoleIdList") List<String> originalRoleIdList, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    List<SysUser> selectUserListByRoleIdsAndDeptIds(@Param("roleIdList") List<String> roleIdList, @Param("deptIdList")List<String> deptIdList, @Param("tenantKey") String tenantKey, @Param("tenantKeyList")List<String> tenantKeyList);

    List<SysUser> selectUserListByUserIdList(@Param("userIdList") List<String> userIdList, @Param("tenantKeyList")List<String> tenantKeyList);

    List<SysUser> selectUserListByOriginalUserIdList(@Param("originalUserIdList") List<String> originalUserIdList, @Param("tenantKey") String tenantKey);

    List<SysUser> selectUserListByNickName(@Param("nickName") String nickName, @Param("tenantKeyList")List<String> tenantKeyList);
}
