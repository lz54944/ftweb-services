package com.hhwy.system.api.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhwy.common.core.annotation.Excel;
import com.hhwy.common.core.annotation.Excel.ColumnType;
import com.hhwy.common.core.annotation.Excel.Type;
import com.hhwy.common.core.annotation.Excels;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.YamlUtil;
import com.hhwy.common.core.web.domain.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

/**
 * 用户对象 sys_user
 *
 * @author hhwy
 */
public class SysUser extends BaseEntity {
    private static List<String> adminRoleNameList;


    private static final long serialVersionUID = 1L;

    static {
        Object adminRoleName = YamlUtil.getProperty("auth.admin-role-name");
        if (adminRoleName != null) {
            adminRoleNameList = Arrays.asList(((String) adminRoleName).split(","));
        }
    }

    /**
     * 用户ID
     */
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /**
     * 用户账号
     */
    @Excel(name = "用户账号")
    private String userName;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String idCard;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 是否是超级管理员(0:不是 1:是)
     */
    private String superFlag;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    /**
     * 兼职部门对象
     */
    private List<SysDept> partDeptList;

    /**
     * 角色对象
     */
    private List<SysRole> roleList;

    /**
     * 岗位对象
     */
    private List<SysPost> postList;

    /**
     * 兼职部门组
     */
    private Long[] partDeptIds;
    private String[] partDeptNames;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 字段描述：所属租户信息
     */
    private SysTenant tenant;

    private boolean checkedAdmin;

    private boolean isAdmin;

    @Override
    public String getTenantKey() {
        if(StringUtils.isEmpty(this.tenantKey)&&tenant!=null){
            return this.tenant.getTenantKey();
        }
        return tenantKey;
    }

    @Override
    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    /** 租户标识 */
    private String tenantKey;

    public SysUser() {
    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static List<String> getAdminRoleNameList() {
        return adminRoleNameList;
    }

    public SysUser checkAdmin() {
        setCheckedAdmin(true);
        if("1".equals(this.superFlag)){
            setIsAdmin(true);
        }else {
            List<SysRole> roleList = this.roleList;
            if(CollectionUtils.isNotEmpty(adminRoleNameList)){
                if(CollectionUtils.isNotEmpty(roleList)){
                    for (SysRole sysRole : roleList) {
                        if(adminRoleNameList.contains(sysRole.getRoleName())){
                            setIsAdmin(true);
                        }
                    }
                }
            }else {
                if(this.userId != null && 1L == this.userId){
                    setIsAdmin(true);
                }
            }
        }
        return this;
    }

    public boolean getCheckedAdmin() {
        return checkedAdmin;
    }

    public void setCheckedAdmin(boolean checkedAdmin) {
        this.checkedAdmin = checkedAdmin;
    }

    public boolean isAdmin() {
        if (getCheckedAdmin()) {
            return isAdmin;
        }else {
            if("1".equals(this.superFlag)){
                return true;
            }else {
                List<SysRole> roleList = this.roleList;
                if(CollectionUtils.isNotEmpty(roleList) && CollectionUtils.isNotEmpty(adminRoleNameList)){
                    for (SysRole sysRole : roleList) {
                        if(adminRoleNameList.contains(sysRole.getRoleName())){
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    /*public boolean isAdmin() {
        return isAdmin(this.roleList);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }*/

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getSuperFlag() {
        return superFlag;
    }

    public void setSuperFlag(String superFlag) {
        this.superFlag = superFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysDept> getPartDeptList() {
        return partDeptList;
    }

    public void setPartDeptList(List<SysDept> partDeptList) {
        this.partDeptList = partDeptList;
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

    public Long[] getPartDeptIds() {
        return partDeptIds;
    }

    public void setPartDeptIds(Long[] partDeptIds) {
        this.partDeptIds = partDeptIds;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public SysTenant getTenant() {
        return tenant;
    }

    public void setTenant(SysTenant tenant) {
        this.tenant = tenant;
    }

    public String[] getPartDeptNames() {
        return partDeptNames;
    }

    public void setPartDeptNames(String[] partDeptNames) {
        this.partDeptNames = partDeptNames;
    }
}
