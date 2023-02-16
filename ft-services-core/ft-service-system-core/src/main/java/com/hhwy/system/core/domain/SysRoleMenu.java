package com.hhwy.system.core.domain;

import com.hhwy.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author hhwy
 */
public class SysRoleMenu extends BaseEntity {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 用于树形控件的层级独立或者层级关联
     * 半选状态(自身没有被选中，选中的是子集菜单)
     * 0：自身被选中  1：自身未被选中，选中的是子集菜单
     */
    private String mistyFlag;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMistyFlag() {
        return mistyFlag;
    }

    public void setMistyFlag(String mistyFlag) {
        this.mistyFlag = mistyFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("menuId", getMenuId())
                .append("mistyFlag", getMistyFlag())
                .toString();
    }
}
