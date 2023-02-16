package com.hhwy.system.api.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 租户管理对象 sys_tenant
 * 
 * @author jzq
 * @date 2021-08-11
 */
public class SysTenant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 租户ID */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long tenantId;

    private String tenantUrl;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 租户标识 */
    @Excel(name = "租户标识")
    private String tenantKey;

    /** 租户状态（0正常 1冻结） 默认0 */
    @Excel(name = "租户状态", readConverterExp = "0=正常,1=冻结")
    private String tenantStatus;

    /** 是否永久有效  0：否  1：是 */
    @Excel(name = "是否永久有效  0：否  1：是")
    private String validityStatus;

    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /** 失效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "失效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactUser;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactNumber;

    /** 管理员账号 对应sys_user中的user_name */
    @Excel(name = "管理员账号 对应sys_user中的user_name")
    private String administratorUserName;

    /** 管理员昵称 对应sys_user中的nick_name */
    @Excel(name = "管理员昵称 对应sys_user中的nick_name")
    private String administratorNickName;

    /**
     * 字段描述：数据库不存储 对应sys_user中的password
     */
    private String administratorPassword;

    /** 菜单/资源 是否与主租户同步 0：不同步 1：同步 */
    @Excel(name = "菜单/资源 是否与主租户同步 0：不同步 1：同步")
    private String menuStatus;

    /** 部门 是否与主租户同步 0：不同步 1：同步 */
    @Excel(name = "部门 是否与主租户同步 0：不同步 1：同步")
    private String deptStatus;

    /** 角色 是否与主租户同步 0：不同步 1：同步 */
    @Excel(name = "角色 是否与主租户同步 0：不同步 1：同步")
    private String roleStatus;

    /** 岗位 是否与主租户同步 0：不同步 1：同步 */
    @Excel(name = "岗位 是否与主租户同步 0：不同步 1：同步")
    private String postStatus;

    /** 用户 是否与主租户同步 0：不同步 1：同步 */
    @Excel(name = "用户 是否与主租户同步 0：不同步 1：同步")
    private String userStatus;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private boolean deptCheckStrictly;

    //选中节点的父级id，多个用","隔开
    private String checkedDeptParent;

    //与checkedDeptParent对应
    private List<String> checkedDeptParentList;

    private List<SysMenu> menuList;

    private List<SysDept> deptList;

    private List<SysRole> roleList;

    private List<SysPost> postList;

    private List<SysUser> userList;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantUrl() {
        return tenantUrl;
    }

    public void setTenantUrl(String tenantUrl) {
        this.tenantUrl = tenantUrl;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(String tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    public String getValidityStatus() {
        return validityStatus;
    }

    public void setValidityStatus(String validityStatus) {
        this.validityStatus = validityStatus;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAdministratorUserName() {
        return administratorUserName;
    }

    public void setAdministratorUserName(String administratorUserName) {
        this.administratorUserName = administratorUserName;
    }

    public String getAdministratorNickName() {
        return administratorNickName;
    }

    public void setAdministratorNickName(String administratorNickName) {
        this.administratorNickName = administratorNickName;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getDeptStatus() {
        return deptStatus;
    }

    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(String roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getAdministratorPassword() {
        return administratorPassword;
    }

    public void setAdministratorPassword(String administratorPassword) {
        this.administratorPassword = administratorPassword;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenu> menuList) {
        this.menuList = menuList;
    }

    public List<SysDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<SysDept> deptList) {
        this.deptList = deptList;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    public List<SysPost> getPostList() {
        return postList;
    }

    public void setPostList(List<SysPost> postList) {
        this.postList = postList;
    }

    public List<SysUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SysUser> userList) {
        this.userList = userList;
    }

    public boolean getMenuCheckStrictly() {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean getDeptCheckStrictly() {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public String getCheckedDeptParent() {
        return checkedDeptParent;
    }

    public void setCheckedDeptParent(String checkedDeptParent) {
        this.checkedDeptParent = checkedDeptParent;
    }

    public List<String> getCheckedDeptParentList() {
        return checkedDeptParentList;
    }

    public void setCheckedDeptParentList(List<String> checkedDeptParentList) {
        this.checkedDeptParentList = checkedDeptParentList;
    }

    public static SysTenant master(){
        SysTenant masterTenant = new SysTenant();
        masterTenant.setTenantName(Constants.MASTER_TENANT_NAME);
        masterTenant.setTenantKey(Constants.MASTER_TENANT_KEY);
        return masterTenant;
    }
}
