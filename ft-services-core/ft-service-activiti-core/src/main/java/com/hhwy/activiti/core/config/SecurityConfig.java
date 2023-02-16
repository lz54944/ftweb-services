package com.hhwy.activiti.core.config;

import org.activiti.api.runtime.shared.security.PrincipalGroupsProvider;
import org.activiti.api.runtime.shared.security.PrincipalIdentityProvider;
import org.activiti.api.runtime.shared.security.PrincipalRolesProvider;
import org.activiti.api.runtime.shared.security.SecurityContextPrincipalProvider;
import org.activiti.core.common.spring.security.LocalSpringSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 15:21
 * <br>备注：无
 */
@Configuration
public class SecurityConfig {

    @Bean
    public LocalSpringSecurityManager localSpringSecurityManager(SecurityContextPrincipalProvider securityContextPrincipalProvider, PrincipalIdentityProvider principalIdentityProvider,
                                                                 PrincipalGroupsProvider principalGroupsProvider, PrincipalRolesProvider principalRolesProvider) {
        return new SelfLocalSpringSecurityManager(securityContextPrincipalProvider, principalIdentityProvider, principalGroupsProvider, principalRolesProvider);
    }

}
