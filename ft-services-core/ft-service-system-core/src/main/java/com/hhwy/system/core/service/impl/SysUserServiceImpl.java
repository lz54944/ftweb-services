package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.exception.CustomException;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.datascope.annotation.DataScope;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.*;
import com.hhwy.system.core.domain.SysUserPartDept;
import com.hhwy.system.core.domain.SysUserPost;
import com.hhwy.system.core.domain.SysUserRole;
import com.hhwy.system.core.mapper.*;
import com.hhwy.system.core.service.ISysConfigService;
import com.hhwy.system.core.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户 业务层处理
 *
 * @author hhwy
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysTenantMapper tenantMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserPartDeptMapper userPartDeptMapper;

    @Autowired
    private SysTenantResourceMapper tenantResourceMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private TokenService tokenService;

   /* public void setTenantInfo(SysUser user){
        user.setTenantKey(tokenService.getTenantKey());
        Map<String, Object> params = user.getParams();
        if(params == null){
            params = new HashMap<>();
        }
        params.put("tenantUserStatus",tokenService.getTenant().getUserStatus());
        user.setParams(params);
    }*/

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user, tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListAll(SysUser user) {
        return userMapper.selectUserListAll(user, tokenService.getTenantKeyListForUser());
    }

    /**
     * 通过用户名查询用户
     *
     * @param tenantKey 租户
     * @param userName  用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String tenantKey, String userName) {
        Assert.notNull(tenantKey, "租户标识不能为空！");
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(tenantKey);
        if (!tenantKey.equals(Constants.MASTER_TENANT_KEY)) {
            SysTenant sysTenant = tenantMapper.selectSysTenantByTenantKey(tenantKey);
            Assert.notNull(sysTenant, "租户(" + tenantKey + ")不存在！");
            if (sysTenant.getUserStatus().equals("1")) {
                tenantKeyList.add(Constants.MASTER_TENANT_KEY);
            }
        }

        SysUser sysUser = userMapper.selectUserByUserName(userName, tenantKey, tenantKeyList);
        fillInfo(sysUser, tenantKey, tenantKeyList);

        if (sysUser == null) {
            return null;
        }
        return sysUser.checkAdmin();
    }

    public void fillInfo(SysUser sysUser, String tenantKey, List<String> tenantKeyList) {
        if (sysUser == null) {
            return;
        }
        SysTenant tenant = sysUser.getTenant();
        if (tenant == null) {
            if (tenantKey.equals(Constants.MASTER_TENANT_KEY)) {
                SysTenant sysTenant = new SysTenant();
                sysTenant.setTenantName(Constants.MASTER_TENANT_NAME);
                sysTenant.setTenantKey(Constants.MASTER_TENANT_KEY);
                sysUser.setTenant(sysTenant);
                sysUser.setTenantKey(sysTenant.getTenantKey());
            } else {
                SysTenant sysTenant = tenantMapper.selectSysTenantByTenantKey(tenantKey);
                Assert.notNull(sysTenant, "租户(" + tenantKey + ")不存在");
                sysUser.setTenant(sysTenant);
                sysUser.setTenantKey(sysTenant.getTenantKey());
            }
        }

        Long deptId = sysUser.getDeptId();
        if (deptId != null) {
            sysUser.setDept(deptMapper.selectDeptById(deptId));
        }

        sysUser.setRoleList(roleMapper.selectRoleListByUserId(sysUser.getUserId(), tenantKey, tenantKeyList));
        List<Long> roleIdList = roleMapper.selectRoleIdListByUserId(sysUser.getUserId(), tenantKey, tenantKeyList);
        if (!CollectionUtils.isEmpty(roleIdList)) {
            Long[] ids = new Long[roleIdList.size()];
            roleIdList.toArray(ids);
            sysUser.setRoleIds(ids);
        }
        List<SysDept> partDeptList = deptMapper.selectPartDeptListByUserId(sysUser.getUserId(), tenantKeyList);
        sysUser.setPartDeptList(partDeptList);
        if (!CollectionUtils.isEmpty(partDeptList)) {
            List<Long> partDeptIdList = new ArrayList<>();
            List<String> partDeptNameList = new ArrayList<>();
            for (SysDept partDept : partDeptList) {
                partDeptIdList.add(partDept.getDeptId());
                partDeptNameList.add(partDept.getDeptName());
            }
            sysUser.setPartDeptIds(partDeptIdList.toArray(new Long[partDeptIdList.size()]));
            sysUser.setPartDeptNames(partDeptNameList.toArray(new String[partDeptNameList.size()]));
        }

        sysUser.setPostList(postMapper.selectPostListByUserId(sysUser.getUserId(), tenantKey, tenantKeyList));
        List<Long> postDeptIdList = postMapper.selectPostIdListByUserId(sysUser.getUserId(), tenantKey, tenantKeyList);
        if (!CollectionUtils.isEmpty(postDeptIdList)) {
            Long[] ids = new Long[postDeptIdList.size()];
            postDeptIdList.toArray(ids);
            sysUser.setPostIds(ids);
        }
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        SysUser sysUser = userMapper.selectUserById(userId, tokenService.getTenantKey()).checkAdmin();
        fillInfo(sysUser, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
        return sysUser;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName, tokenService.getTenantKey(), tokenService.getTenantKeyListForRole());
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName, tokenService.getTenantKey(), tokenService.getTenantKeyListForPost());
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName, tokenService.getTenantKeyListForUser());
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkUserNameUniqueByTenantKey(String tenantKey, String userName) {
        int count = userMapper.checkUserNameUniqueByTenantKey(tenantKey, userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhoneNumber(), tokenService.getTenantKeyListForUser());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail(), tokenService.getTenantKeyListForUser());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        user.setTenantKey(tokenService.getTenantKey());
        if (StringUtils.isBlank(user.getPassword())) {
            String password = configService.selectConfigByKey("sys.user.initPassword");
            user.setPassword(password);//默认密码为用户账号
        }
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        // 新增用户兼职部门
        insertUserPartDept(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId, tokenService.getTenantKey());
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId, tokenService.getTenantKey());
        // 新增用户与岗位管理
        insertUserPost(user);
        // 删除用户兼职部门信息
        userPartDeptMapper.deleteSysUserPartDeptByUserId(userId, tokenService.getTenantKey());
        // 新增用户兼职部门信息
        insertUserPartDept(user);
        int rows = userMapper.updateUser(user);
        if (rows > 0) {
            tokenService.setRoleAndPermissionUpdateCollection();
        }
        return rows;
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                ur.setTenantKey(tokenService.getTenantKey());
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                up.setTenantKey(tokenService.getTenantKey());
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 新增用户兼职部门信息
     *
     * @param user 用户对象
     */
    public void insertUserPartDept(SysUser user) {
        Long[] partDeptIds = user.getPartDeptIds();
        if (StringUtils.isNotNull(partDeptIds)) {
            // 新增用户与岗位管理
            List<SysUserPartDept> list = new ArrayList<>();
            for (Long partDeptId : partDeptIds) {
                SysUserPartDept upd = new SysUserPartDept();
                upd.setUserId(user.getUserId());
                upd.setDeptId(partDeptId);
                upd.setTenantKey(tokenService.getTenantKey());
                list.add(upd);
            }
            if (list.size() > 0) {
                userPartDeptMapper.batchInsertSysUserPartDept(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId, tokenService.getTenantKey());
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId, tokenService.getTenantKey());
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds, tokenService.getTenantKey());
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds, tokenService.getTenantKey());
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            user.setTenantKey(tokenService.getTenantKey());
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName(), tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
                if (StringUtils.isNull(u)) {
                    user.setCreateUser(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateUser(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user, Long roleId) {
        return userMapper.selectAllocatedList(user, roleId, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user, Long roleId) {
        return userMapper.selectUnallocatedList(user, roleId, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByRoleIds(String roleIds) {
        List<String> roleIdList = Arrays.asList(roleIds.split(","));
        return userMapper.selectUserListByRoleIdList(roleIdList, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByOriginalRoleIds(String originalRoleIds) {
        List<String> originalRoleIdList = Arrays.asList(originalRoleIds.split(","));
        List<SysUser> result = new ArrayList<>();
        List<SysUser> sysUserListM = userMapper.selectUserListByRoleIdList(originalRoleIdList, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
        List<SysUser> sysUserListT = userMapper.selectUserListByOriginalRoleIdList(originalRoleIdList, tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
        if (!CollectionUtils.isEmpty(sysUserListM)) {
            result.addAll(sysUserListM);
        }
        if (!CollectionUtils.isEmpty(sysUserListT)) {
            result.addAll(sysUserListT);
        }
        return result;
    }

    @Override
    public List<SysUser> selectUserListByRoleIdsAndDeptIds(String roleIds, String deptIds) {
        List<String> roleIdList = Arrays.asList(roleIds.split(","));
        List<String> deptIdList = Arrays.asList(deptIds.split(","));
        return userMapper.selectUserListByRoleIdsAndDeptIds(roleIdList, deptIdList,
                tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByOriginalRoleIdsAndDeptIds(String originalRoleIds, String deptIds) {
        List<String> originalRoleIdList = Arrays.asList(originalRoleIds.split(","));
        List<String> deptIdList = Arrays.asList(deptIds.split(","));

        List<String> roleIdList = new ArrayList<>();
        List<String> roleIdListT = tenantResourceMapper.selectResourceIdByResourceTableAndOriginalIdList(tokenService.getTenantKey(), "sys_role", originalRoleIdList);
        List<String> roleIdListM = roleMapper.selectSelfRoleIdListByRoleIdList(originalRoleIdList, tokenService.getTenantKeyListForRole());
        if (!CollectionUtils.isEmpty(roleIdListT)) {
            roleIdList.addAll(roleIdListT);
        }
        if (!CollectionUtils.isEmpty(roleIdListM)) {
            roleIdList.addAll(roleIdListM);
        }

        return userMapper.selectUserListByRoleIdsAndDeptIds(roleIdList, deptIdList,
                tokenService.getTenantKey(), tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByUserIds(String userIds) {
        List<String> userIdList = Arrays.asList(userIds.split(","));
        return userMapper.selectUserListByUserIdList(userIdList, tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByOriginalUserIds(String originalUserIds) {
        List<String> originalUserIdList = Arrays.asList(originalUserIds.split(","));
        List<SysUser> result = new ArrayList<>();
        List<SysUser> sysUserListM = userMapper.selectUserListByUserIdList(originalUserIdList, tokenService.getTenantKeyListForUser());
        List<SysUser> sysUserListT = userMapper.selectUserListByOriginalUserIdList(originalUserIdList, tokenService.getTenantKey());
        if (!CollectionUtils.isEmpty(sysUserListM)) {
            result.addAll(sysUserListM);
        }
        if (!CollectionUtils.isEmpty(sysUserListT)) {
            result.addAll(sysUserListT);
        }
        return result;
    }

    @Override
    public List<SysUser> selectUserListByNickName(String nickName) {
        return userMapper.selectUserListByNickName(nickName, tokenService.getTenantKeyListForUser());
    }

    @Override
    public List<SysUser> selectUserListByUsernames(String tenantKey, String usernames) {
        List<SysUser> result = new ArrayList<>();
        List<String> usernameList = Arrays.asList(usernames.split(","));
        if (!CollectionUtils.isEmpty(usernameList)) {
            for (String username : usernameList) {
                SysUser sysUser = selectUserByUserName(tenantKey, username);
                if (sysUser != null) {
                    result.add(sysUser);
                }
            }
        }
        return result;
    }
}
