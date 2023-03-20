package com.hhwy.system.core.service.impl;

import com.google.common.collect.Lists;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.exception.BaseException;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.tenant.service.TenantSocketIOServerService;
import com.hhwy.system.api.domain.*;
import com.hhwy.system.core.domain.SysConfig;
import com.hhwy.system.core.domain.SysTenantResource;
import com.hhwy.system.core.mapper.*;
import com.hhwy.system.core.service.ISysTenantService;
import com.hhwy.system.core.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 租户管理Service业务层处理
 *
 * @author jzq
 * @date 2021-08-11
 */

public class SysTenantServiceImpl implements ISysTenantService {
    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired
    private SysTenantResourceMapper sysTenantResourceMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private TenantSocketIOServerService tenantSocketIOServerService;

    @Autowired
    private ISysUserService userService;
    /**
     * 查询租户管理
     *
     * @param tenantId 租户管理ID
     * @return 租户管理
     */
    @Override
    public SysTenant selectSysTenantById(Long tenantId) {
        SysTenant sysTenant = sysTenantMapper.selectSysTenantById(tenantId);
        menuSelectResolver(sysTenant);
        deptSelectResolver(sysTenant);
        roleSelectResolver(sysTenant);
        postSelectResolver(sysTenant);
        userSelectResolver(sysTenant);
        String checkedDeptParent = sysTenant.getCheckedDeptParent();
        if (StringUtils.isNotEmpty(checkedDeptParent)) {
            List<String> checkedDeptParentList = Arrays.asList(checkedDeptParent.split(","));
            sysTenant.setCheckedDeptParentList(checkedDeptParentList);
        }
        return sysTenant;
    }

    @Override
    public SysTenant selectSysTenantByTenantKey(String tenantKey) {
        SysTenant sysTenant;
        if (Constants.MASTER_TENANT_KEY.equals(tenantKey)) {
            sysTenant = SysTenant.master();
        }else {
            sysTenant = sysTenantMapper.selectSysTenantByTenantKey(tenantKey);
        }
        Assert.notNull(sysTenant,"租户("+tenantKey+")不存在！");
        menuSelectResolver(sysTenant);
        deptSelectResolver(sysTenant);
        roleSelectResolver(sysTenant);
        postSelectResolver(sysTenant);
        userSelectResolver(sysTenant);
        return sysTenant;
    }

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant) {
        SysConfig sysConfig = configMapper.selectConfigByKey("tenant.rootUrl");
        List<SysTenant> sysTenants = sysTenantMapper.selectSysTenantList(sysTenant);
        if(sysConfig != null){
            String rootUrl = sysConfig.getConfigValue();
            for (SysTenant tenant : sysTenants) {
                String tenantKey = tenant.getTenantKey();
                tenant.setTenantUrl(rootUrl+"/"+tenantKey+"login");
            }
        }
        return sysTenants;
    }

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysTenant(SysTenant sysTenant) {
        List<String> checkedDeptParentList = sysTenant.getCheckedDeptParentList();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(checkedDeptParentList)) {
            String checkedDeptParent = StringUtils.join(checkedDeptParentList, ",");
            sysTenant.setCheckedDeptParent(checkedDeptParent);
        }
        menuInsertResolver(sysTenant);
        deptInsertResolver(sysTenant);
        roleInsertResolver(sysTenant);
        postInsertResolver(sysTenant);
        userInsertResolver(sysTenant);
        sysTenant.setCreateTime(DateUtils.getNowDate());
        int result = sysTenantMapper.insertSysTenant(sysTenant);
        if (result>0) {
            if(StringUtils.isNotEmpty(sysTenant.getAdministratorUserName())){
                SysUser admin = new SysUser();
                admin.setSuperFlag("1");
                admin.setTenantKey(sysTenant.getTenantKey());
                admin.setUserName(sysTenant.getAdministratorUserName());
                admin.setNickName(sysTenant.getAdministratorNickName());
                admin.setPassword(SecurityUtils.encryptPassword(sysTenant.getAdministratorPassword()));
                sysUserMapper.insertUser(admin);
            }
            tenantSocketIOServerService.broadcastCreatedMessage(sysTenant);
        }
        return result;
    }

    @Transactional
    public void menuSelectResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<SysMenu> menuList = sysMenuMapper.selectMenuListByTenantKey(tenantKey);
        sysTenant.setMenuList(menuList);
    }

    @Transactional
    public void menuInsertResolver(SysTenant sysTenant){
        String menuStatus = sysTenant.getMenuStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(menuStatus)){ //0：不同步 1：同步
            List<SysMenu> menuList = sysTenant.getMenuList();
            if(!CollectionUtils.isEmpty(menuList)){
                List<SysMenu> originalMenuList = new ArrayList<>();
                Map<Long,Long> menuIdOldKeyNewValMap = new HashMap<>();
                for (SysMenu sysMenu : menuList) {
                    SysMenu originalMenu = sysMenuMapper.selectMenuById(sysMenu.getMenuId());
                    originalMenu.setMenuId(null);
                    originalMenu.setTenantKey(sysTenant.getTenantKey());
                    sysMenuMapper.insertMenu(originalMenu);
                    originalMenuList.add(originalMenu);
                    menuIdOldKeyNewValMap.put(sysMenu.getMenuId(),originalMenu.getMenuId());

                    Map<String, Object> params = new HashMap<>();
                    params.put("resourceId",originalMenu.getMenuId());
                    sysMenu.setParams(params);
                }
                sysTenantResourceMapper.insertSysTenantResourceMenuList(tenantKey,menuList);
                for (SysMenu originalMenu : originalMenuList) {
                    Long newParentId = menuIdOldKeyNewValMap.get(originalMenu.getParentId());
                    originalMenu.setParentId(newParentId);
                    sysMenuMapper.updateMenu(originalMenu);
                }
            }
        }else if ("1".equals(menuStatus)){

        }else {
            throw new RuntimeException("menuStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void menuUpdateResolver(SysTenant sysTenant){
        String menuStatus = sysTenant.getMenuStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(menuStatus)){
            List<Long> menuDeletingList = new ArrayList<>();
            List<Long> tenantResourceDeletingList = new ArrayList<>();
            List<SysMenu> addingList = new ArrayList<>();

            Map<Long,SysTenantResource> tenantResourceMap = new HashMap<>();
            List<SysTenantResource> tenantResourceList = sysTenantResourceMapper.selectSysTenantResourceListByTenantKeyAndTableName(tenantKey, "sys_menu");
            for (SysTenantResource tenantResource : tenantResourceList) {
                Long resourceOriginalId = tenantResource.getResourceOriginalId();
                tenantResourceMap.put(resourceOriginalId,tenantResource);
            }

            List<SysMenu> menuList = sysTenant.getMenuList();
            for (SysMenu sysMenu : menuList) {
                Long menuId = sysMenu.getMenuId();
                SysTenantResource sysTenantResource = tenantResourceMap.get(menuId);
                if (sysTenantResource==null) {
                    addingList.add(sysMenu);
                }else {
                    tenantResourceList.remove(sysTenantResource);
                }
            }
            if(!CollectionUtils.isEmpty(tenantResourceList)){
                for (SysTenantResource sysTenantResource : tenantResourceList) {
                    menuDeletingList.add(sysTenantResource.getResourceId());
                    tenantResourceDeletingList.add(sysTenantResource.getId());
                }
            }
            if(!CollectionUtils.isEmpty(menuDeletingList)){
                sysMenuMapper.realDeleteMenuByIds(menuDeletingList);
                sysTenantResourceMapper.deleteSysTenantResourceByIds(tenantResourceDeletingList);
            }
            if(!CollectionUtils.isEmpty(addingList)){
                sysTenant.setMenuList(addingList);
                menuInsertResolver(sysTenant);
            }
        }else if ("1".equals(menuStatus)){
            List<Long> menuIdList = sysTenantResourceMapper.selectMenuIdListByTenantKey(tenantKey);
            if(!CollectionUtils.isEmpty(menuIdList)){
                sysMenuMapper.realDeleteMenuByIds(menuIdList);
                sysTenantResourceMapper.deleteSysTenantResourceMenuByTenantKey(tenantKey);
            }
        }else {
            throw new RuntimeException("menuStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void menuDeleteResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<Long> menuIdList = sysTenantResourceMapper.selectMenuIdListByTenantKey(tenantKey);
        sysMenuMapper.realDeleteMenuByIds(menuIdList);
        sysTenantResourceMapper.deleteSysTenantResourceMenuByTenantKey(tenantKey);
    }

    @Transactional
    public void deptSelectResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<SysDept> deptList = sysDeptMapper.selectDeptListByTenantKey(tenantKey);
        sysTenant.setDeptList(deptList);
    }

    @Transactional
    public void deptInsertResolver(SysTenant sysTenant){
        String deptStatus = sysTenant.getDeptStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(deptStatus)){
            List<SysDept> deptList = sysTenant.getDeptList();
            if (!CollectionUtils.isEmpty(deptList)) {
                List<SysDept> originalDeptList = new ArrayList<>();
                Map<Long,Long> deptIdOldKeyNewValMap = new HashMap<>();
                for (SysDept sysDept : deptList) {
                    SysDept originalDept = sysDeptMapper.selectDeptById(sysDept.getDeptId());
                    originalDept.setDeptId(null);
                    originalDept.setTenantKey(sysTenant.getTenantKey());
                    sysDeptMapper.insertDept(originalDept);
                    originalDeptList.add(originalDept);
                    deptIdOldKeyNewValMap.put(sysDept.getDeptId(),originalDept.getDeptId());

                    Map<String, Object> params = new HashMap<>();
                    params.put("resourceId",originalDept.getDeptId());
                    sysDept.setParams(params);
                }
                sysTenantResourceMapper.insertSysTenantResourceDeptList(tenantKey,deptList);
                for (SysDept originalDept : originalDeptList) {
                    Long newParentId = deptIdOldKeyNewValMap.get(originalDept.getParentId());
                    originalDept.setParentId(newParentId);
                    String ancestors = originalDept.getAncestors();
                    if (StringUtils.isNotEmpty(ancestors)) {
                        String newAncestors = "";
                        String[] ids = ancestors.split(",");
                        for (String id : ids) {
                            if(StringUtils.isNotEmpty(id)){
                                Long newId = deptIdOldKeyNewValMap.get(Long.parseLong(id));
                                if (newId!=null) {
                                    newAncestors += newId+",";
                                }else {
                                    newAncestors += id+",";
                                }
                            }
                        }
                        originalDept.setAncestors(newAncestors.substring(0,newAncestors.length()-1));
                    }
                    sysDeptMapper.updateDept(originalDept);
                }
            }
        }else if ("1".equals(deptStatus)){

        }else {
            throw new RuntimeException("deptStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void deptUpdateResolver(SysTenant sysTenant){
        String deptStatus = sysTenant.getDeptStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(deptStatus)){
            List<Long> deptDeletingList = new ArrayList<>();
            List<Long> tenantResourceDeletingList = new ArrayList<>();
            List<SysDept> addingList = new ArrayList<>();

            Map<Long,SysTenantResource> tenantResourceMap = new HashMap<>();
            List<SysTenantResource> tenantResourceList = sysTenantResourceMapper.selectSysTenantResourceListByTenantKeyAndTableName(tenantKey, "sys_dept");
            for (SysTenantResource tenantResource : tenantResourceList) {
                Long resourceOriginalId = tenantResource.getResourceOriginalId();
                tenantResourceMap.put(resourceOriginalId,tenantResource);
            }

            List<SysDept> deptList = sysTenant.getDeptList();
            for (SysDept sysDept : deptList) {
                Long deptId = sysDept.getDeptId();
                SysTenantResource sysTenantResource = tenantResourceMap.get(deptId);
                if (sysTenantResource==null) {
                    addingList.add(sysDept);
                }else {
                    tenantResourceList.remove(sysTenantResource);
                }
            }

            if(!CollectionUtils.isEmpty(tenantResourceList)){
                for (SysTenantResource sysTenantResource : tenantResourceList) {
                    deptDeletingList.add(sysTenantResource.getResourceId());
                    tenantResourceDeletingList.add(sysTenantResource.getId());
                }
            }

            if(!CollectionUtils.isEmpty(deptDeletingList)){
                sysDeptMapper.realDeleteDeptByIds(deptDeletingList);
                sysTenantResourceMapper.deleteSysTenantResourceByIds(tenantResourceDeletingList);
            }
            if(!CollectionUtils.isEmpty(addingList)){
                sysTenant.setDeptList(addingList);
                deptInsertResolver(sysTenant);
            }

        }else if ("1".equals(deptStatus)){
            List<Long> deptIdList = sysTenantResourceMapper.selectDeptIdListByTenantKey(tenantKey);
            if(!CollectionUtils.isEmpty(deptIdList)){
                sysDeptMapper.realDeleteDeptByIds(deptIdList);
                sysTenantResourceMapper.deleteSysTenantResourceDeptByTenantKey(tenantKey);
            }
        }else {
            throw new RuntimeException("deptStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void deptDeleteResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<Long> deptIdList = sysTenantResourceMapper.selectDeptIdListByTenantKey(tenantKey);
        if (!CollectionUtils.isEmpty(deptIdList)) {
            sysDeptMapper.realDeleteDeptByIds(deptIdList);
        }
        sysTenantResourceMapper.deleteSysTenantResourceDeptByTenantKey(tenantKey);
    }

    @Transactional
    public void roleSelectResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<SysRole> roleList = sysRoleMapper.selectRoleListByTenantKey(tenantKey);
        sysTenant.setRoleList(roleList);
    }

    @Transactional
    public void roleInsertResolver(SysTenant sysTenant){
        String roleStatus = sysTenant.getRoleStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(roleStatus)){
            List<SysRole> roleList = sysTenant.getRoleList();
            if (!CollectionUtils.isEmpty(roleList)) {
                for (SysRole sysRole : roleList) {
                    SysRole originalRole = sysRoleMapper.selectRoleById(sysRole.getRoleId());
                    originalRole.setRoleId(null);
                    originalRole.setTenantKey(sysTenant.getTenantKey());
                    sysRoleMapper.insertRole(originalRole);

                    Map<String, Object> params = new HashMap<>();
                    params.put("resourceId",originalRole.getRoleId());
                    sysRole.setParams(params);
                }
                sysTenantResourceMapper.insertSysTenantResourceRoleList(tenantKey,roleList);
            }
        }else if ("1".equals(roleStatus)){

        }else {
            throw new RuntimeException("roleStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void roleUpdateResolver(SysTenant sysTenant){
        String roleStatus = sysTenant.getRoleStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(roleStatus)){
            List<Long> roleDeletingList = new ArrayList<>();
            List<Long> tenantResourceDeletingList = new ArrayList<>();
            List<SysRole> addingList = new ArrayList<>();

            Map<Long,SysTenantResource> tenantResourceMap = new HashMap<>();
            List<SysTenantResource> tenantResourceList = sysTenantResourceMapper.selectSysTenantResourceListByTenantKeyAndTableName(tenantKey, "sys_role");
            for (SysTenantResource tenantResource : tenantResourceList) {
                Long resourceOriginalId = tenantResource.getResourceOriginalId();
                tenantResourceMap.put(resourceOriginalId,tenantResource);
            }

            List<SysRole> roleList = sysTenant.getRoleList();
            for (SysRole sysRole : roleList) {
                Long roleId = sysRole.getRoleId();
                SysTenantResource sysTenantResource = tenantResourceMap.get(roleId);
                if (sysTenantResource==null) {
                    addingList.add(sysRole);
                }else {
                    tenantResourceList.remove(sysTenantResource);
                }
            }
            if(!CollectionUtils.isEmpty(tenantResourceList)){
                for (SysTenantResource sysTenantResource : tenantResourceList) {
                    roleDeletingList.add(sysTenantResource.getResourceId());
                    tenantResourceDeletingList.add(sysTenantResource.getId());
                }
            }
            if(!CollectionUtils.isEmpty(roleDeletingList)){
                sysRoleMapper.realDeleteRoleByIds(roleDeletingList);
                sysTenantResourceMapper.deleteSysTenantResourceByIds(tenantResourceDeletingList);
            }
            if(!CollectionUtils.isEmpty(addingList)){
                sysTenant.setRoleList(addingList);
                roleInsertResolver(sysTenant);
            }
        }else if ("1".equals(roleStatus)){
            List<Long> roleIdList = sysTenantResourceMapper.selectRoleIdListByTenantKey(tenantKey);
            if (!CollectionUtils.isEmpty(roleIdList)) {
                sysRoleMapper.realDeleteRoleByIds(roleIdList);
                sysTenantResourceMapper.deleteSysTenantResourceRoleByTenantKey(tenantKey);
            }
        }else {
            throw new RuntimeException("roleStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void roleDeleteResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<Long> roleIdList = sysTenantResourceMapper.selectRoleIdListByTenantKey(tenantKey);
        if (!CollectionUtils.isEmpty(roleIdList)) {
            sysRoleMapper.realDeleteRoleByIds(roleIdList);
        }
        sysTenantResourceMapper.deleteSysTenantResourceRoleByTenantKey(tenantKey);
    }

    @Transactional
    public void postSelectResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<SysPost> postList = sysPostMapper.selectPostListByTenantKey(tenantKey);
        sysTenant.setPostList(postList);
    }

    @Transactional
    public void postInsertResolver(SysTenant sysTenant){
        String postStatus = sysTenant.getPostStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(postStatus)){
            List<SysPost> postList = sysTenant.getPostList();
            if (!CollectionUtils.isEmpty(postList)) {
                for (SysPost sysPost : postList) {
                    SysPost originalPost = sysPostMapper.selectPostById(sysPost.getPostId());
                    originalPost.setPostId(null);
                    originalPost.setTenantKey(sysTenant.getTenantKey());
                    sysPostMapper.insertPost(originalPost);

                    Map<String, Object> params = new HashMap<>();
                    params.put("resourceId",originalPost.getPostId());
                    sysPost.setParams(params);
                }
                sysTenantResourceMapper.insertSysTenantResourcePostList(tenantKey,postList);
            }
        }else if ("1".equals(postStatus)){

        }else {
            throw new RuntimeException("postStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void postUpdateResolver(SysTenant sysTenant){
        String postStatus = sysTenant.getPostStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(postStatus)){
            List<Long> postDeletingList = new ArrayList<>();
            List<Long> tenantResourceDeletingList = new ArrayList<>();
            List<SysPost> addingList = new ArrayList<>();

            Map<Long,SysTenantResource> tenantResourceMap = new HashMap<>();
            List<SysTenantResource> tenantResourceList = sysTenantResourceMapper.selectSysTenantResourceListByTenantKeyAndTableName(tenantKey, "sys_post");
            for (SysTenantResource tenantResource : tenantResourceList) {
                Long resourceOriginalId = tenantResource.getResourceOriginalId();
                tenantResourceMap.put(resourceOriginalId,tenantResource);
            }

            List<SysPost> postList = sysTenant.getPostList();
            for (SysPost sysPost : postList) {
                Long postId = sysPost.getPostId();
                SysTenantResource sysTenantResource = tenantResourceMap.get(postId);
                if (sysTenantResource==null) {
                    addingList.add(sysPost);
                }else {
                    tenantResourceList.remove(sysTenantResource);
                }
            }
            if(!CollectionUtils.isEmpty(tenantResourceList)){
                for (SysTenantResource sysTenantResource : tenantResourceList) {
                    postDeletingList.add(sysTenantResource.getResourceId());
                    tenantResourceDeletingList.add(sysTenantResource.getId());
                }
            }
            if(!CollectionUtils.isEmpty(postDeletingList)){
                sysPostMapper.realDeletePostByIds(postDeletingList);
                sysTenantResourceMapper.deleteSysTenantResourceByIds(tenantResourceDeletingList);
            }
            if(!CollectionUtils.isEmpty(addingList)){
                sysTenant.setPostList(addingList);
                postInsertResolver(sysTenant);
            }
        }else if ("1".equals(postStatus)){
            List<Long> postIdList = sysTenantResourceMapper.selectPostIdListByTenantKey(tenantKey);
            if (!CollectionUtils.isEmpty(postIdList)) {
                sysPostMapper.realDeletePostByIds(postIdList);
                sysTenantResourceMapper.deleteSysTenantResourcePostByTenantKey(tenantKey);
            }
        }else {
            throw new RuntimeException("postStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void postDeleteResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<Long> postIdList = sysTenantResourceMapper.selectPostIdListByTenantKey(tenantKey);
        if (!CollectionUtils.isEmpty(postIdList)) {
            sysPostMapper.realDeletePostByIds(postIdList);
        }
        sysTenantResourceMapper.deleteSysTenantResourcePostByTenantKey(tenantKey);
    }

    @Transactional
    public void userSelectResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<SysUser> userList = sysUserMapper.selectUserListByTenantKey(tenantKey);
        sysTenant.setUserList(userList);
    }

    @Transactional
    public void userInsertResolver(SysTenant sysTenant){
        String userStatus = sysTenant.getUserStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(userStatus)){
            List<SysUser> userList = sysTenant.getUserList();
            if (!CollectionUtils.isEmpty(userList)) {
                for (SysUser sysUser : userList) {
                    SysUser originalUser = sysUserMapper.selectUserById(sysUser.getUserId(),tenantKey);
                    originalUser.setUserId(null);
                    originalUser.setTenantKey(sysTenant.getTenantKey());
                    originalUser.setDeptId(null);
                    sysUserMapper.insertUser(originalUser);

                    Map<String, Object> params = new HashMap<>();
                    params.put("resourceId",originalUser.getUserId());
                    sysUser.setParams(params);
                }
                sysTenantResourceMapper.insertSysTenantResourceUserList(tenantKey,userList);
            }
        }else if ("1".equals(userStatus)){

        }else {
            throw new RuntimeException("userStatus非法，只能是0或1");
        }
    }

    @Transactional
    public void userDeleteResolver(SysTenant sysTenant){
        String tenantKey = sysTenant.getTenantKey();
        List<Long> userIdList = sysTenantResourceMapper.selectUserIdListByTenantKey(tenantKey);
        if (!CollectionUtils.isEmpty(userIdList)) {
            sysUserMapper.realDeleteUserByIds(userIdList);
        }
        sysTenantResourceMapper.deleteSysTenantResourceUserByTenantKey(tenantKey);
    }

    @Transactional
    public void userUpdateResolver(SysTenant sysTenant){
        String userStatus = sysTenant.getUserStatus();
        String tenantKey = sysTenant.getTenantKey();
        if("0".equals(userStatus)){
            List<Long> userDeletingList = new ArrayList<>();
            List<Long> tenantResourceDeletingList = new ArrayList<>();
            List<SysUser> addingList = new ArrayList<>();

            Map<Long,SysTenantResource> tenantResourceMap = new HashMap<>();
            List<SysTenantResource> tenantResourceList = sysTenantResourceMapper.selectSysTenantResourceListByTenantKeyAndTableName(tenantKey, "sys_user");
            for (SysTenantResource tenantResource : tenantResourceList) {
                Long resourceOriginalId = tenantResource.getResourceOriginalId();
                tenantResourceMap.put(resourceOriginalId,tenantResource);
            }

            List<SysUser> userList = sysTenant.getUserList();
            for (SysUser sysUser : userList) {
                Long userId = sysUser.getUserId();
                SysTenantResource sysTenantResource = tenantResourceMap.get(userId);
                if (sysTenantResource==null) {
                    addingList.add(sysUser);
                }else {
                    tenantResourceList.remove(sysTenantResource);
                }
            }
            if(!CollectionUtils.isEmpty(tenantResourceList)){
                for (SysTenantResource sysTenantResource : tenantResourceList) {
                    userDeletingList.add(sysTenantResource.getResourceId());
                    tenantResourceDeletingList.add(sysTenantResource.getId());
                }
            }
            if(!CollectionUtils.isEmpty(userDeletingList)){
                sysUserMapper.realDeleteUserByIds(userDeletingList);
                sysTenantResourceMapper.deleteSysTenantResourceByIds(tenantResourceDeletingList);
            }
            if(!CollectionUtils.isEmpty(addingList)){
                sysTenant.setUserList(addingList);
                userInsertResolver(sysTenant);
            }
        }else if ("1".equals(userStatus)){
            List<Long> userIdList = sysTenantResourceMapper.selectUserIdListByTenantKey(tenantKey);
            if (!CollectionUtils.isEmpty(userIdList)) {
                sysUserMapper.realDeleteUserByIds(userIdList);
                sysTenantResourceMapper.deleteSysTenantResourceUserByTenantKey(tenantKey);
            }
        }else {
            throw new RuntimeException("userStatus非法，只能是0或1");
        }
    }

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysTenant(SysTenant sysTenant) {
        List<String> checkedDeptParentList = sysTenant.getCheckedDeptParentList();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(checkedDeptParentList)) {
            String checkedDeptParent = StringUtils.join(checkedDeptParentList, ",");
            sysTenant.setCheckedDeptParent(checkedDeptParent);
        }
        SysTenant oldSysTenant = sysTenantMapper.selectSysTenantById(sysTenant.getTenantId());
        String oldAdministratorUserName = oldSysTenant.getAdministratorUserName();
        if(oldAdministratorUserName != null && !oldAdministratorUserName.equals(sysTenant.getAdministratorUserName())){
            if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysTenant.getAdministratorUserName()))
                    ||UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUniqueByTenantKey(sysTenant.getTenantKey(),sysTenant.getAdministratorUserName()))) {
                throw new BaseException("管理员'" + sysTenant.getAdministratorUserName() + "'添加失败，账号已存在");
            }
        }
        menuUpdateResolver(sysTenant);
        deptUpdateResolver(sysTenant);
        roleUpdateResolver(sysTenant);
        postUpdateResolver(sysTenant);
        userUpdateResolver(sysTenant);
        sysTenant.setUpdateTime(DateUtils.getNowDate());
        int result = sysTenantMapper.updateSysTenant(sysTenant);
        if (result>0) {
            String oldAdministratorNickName = oldSysTenant.getAdministratorNickName();
            String oldAdministratorPassword = oldSysTenant.getAdministratorPassword();

            List<String> tenantKeyList = new ArrayList<>();
            tenantKeyList.add(sysTenant.getTenantKey());
            SysUser admin = sysUserMapper.selectUserByUserName(sysTenant.getAdministratorUserName(),sysTenant.getTenantKey(),tenantKeyList);

            if(oldAdministratorUserName != null && oldAdministratorUserName.equals(sysTenant.getAdministratorUserName())
                && oldAdministratorNickName.equals(sysTenant.getAdministratorNickName())
                && StringUtils.isEmpty(oldAdministratorPassword)
                && admin != null){
            }else {
                if (admin==null){
                    admin = new SysUser();
                    admin.setSuperFlag("1");
                    admin.setTenantKey(sysTenant.getTenantKey());
                    admin.setUserName(sysTenant.getAdministratorUserName());
                    admin.setNickName(sysTenant.getAdministratorNickName());
                    admin.setPassword(SecurityUtils.encryptPassword(sysTenant.getAdministratorPassword()));
                    sysUserMapper.insertUser(admin);
                }else {
                    admin.setUserName(sysTenant.getAdministratorUserName());
                    admin.setNickName(sysTenant.getAdministratorNickName());
                    admin.setPassword(SecurityUtils.encryptPassword(sysTenant.getAdministratorPassword()));
                    sysUserMapper.updateUser(admin);
                }
            }
            /*//租户修改不需要做任何处理
            tenantSocketIOServerService.broadcastEditedMessage(sysTenant);*/
        }
        return result;
    }

    /**
     * 删除租户管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysTenantByIds(String ids) {
        List<SysTenant> sysTenantList= new ArrayList<>();
        String[] tenantIdArr = Convert.toStrArray(ids);
        for (String tenantId : tenantIdArr) {
            SysTenant sysTenant = sysTenantMapper.selectSysTenantById(Long.parseLong(tenantId));
            sysTenantList.add(sysTenant);
            menuDeleteResolver(sysTenant);
            deptDeleteResolver(sysTenant);
            roleDeleteResolver(sysTenant);
            postDeleteResolver(sysTenant);
            userDeleteResolver(sysTenant);

            List<String> tenantKeyList = new ArrayList<>();
            tenantKeyList.add(sysTenant.getTenantKey());
            SysUser admin = sysUserMapper.selectUserByUserName(sysTenant.getAdministratorUserName(),sysTenant.getTenantKey(),tenantKeyList);
            sysUserMapper.realDeleteUserById(admin.getUserId());
        }
        int result = sysTenantMapper.deleteSysTenantByIds(Convert.toStrArray(ids));
        if (result>0) {
            tenantSocketIOServerService.broadcastDeletedMessage(sysTenantList);
        }
        return result;
    }

    /**
     * 删除租户管理信息
     *
     * @param tenantId 租户管理ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysTenantById(Long tenantId) {
        SysTenant sysTenant = sysTenantMapper.selectSysTenantById(tenantId);
        menuDeleteResolver(sysTenant);
        deptDeleteResolver(sysTenant);
        roleDeleteResolver(sysTenant);
        postDeleteResolver(sysTenant);
        userDeleteResolver(sysTenant);
        int result = sysTenantMapper.deleteSysTenantById(tenantId);
        if (result>0) {
            List<SysTenant> sysTenantList= Lists.newArrayList(sysTenant);
            tenantSocketIOServerService.broadcastDeletedMessage(sysTenantList);
        }
        return result;
    }
}
