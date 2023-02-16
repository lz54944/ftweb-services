package com.hhwy.activiti.core.config;

import com.hhwy.common.core.utils.SecurityUtils;
import org.activiti.api.runtime.shared.security.PrincipalGroupsProvider;
import org.activiti.api.runtime.shared.security.PrincipalIdentityProvider;
import org.activiti.api.runtime.shared.security.PrincipalRolesProvider;
import org.activiti.api.runtime.shared.security.SecurityContextPrincipalProvider;
import org.activiti.core.common.spring.security.LocalSpringSecurityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 15:59
 * <br>备注：无
 */
public class SelfLocalSpringSecurityManager extends LocalSpringSecurityManager {

    public SelfLocalSpringSecurityManager(SecurityContextPrincipalProvider securityContextPrincipalProvider, PrincipalIdentityProvider principalIdentityProvider, PrincipalGroupsProvider principalGroupsProvider, PrincipalRolesProvider principalRolesProvider) {
        super(securityContextPrincipalProvider, principalIdentityProvider, principalGroupsProvider, principalRolesProvider);
    }

    @Override
    public String getAuthenticatedUserId() {
        return SecurityUtils.getUserId().toString();
    }

    @Override
    public List<String> getAuthenticatedUserGroups() {
        List<String> group = new ArrayList<>();
        group.add("all");
        return group;
    }

    @Override
    public List<String> getAuthenticatedUserRoles() {
        return super.getAuthenticatedUserRoles();
    }
}
