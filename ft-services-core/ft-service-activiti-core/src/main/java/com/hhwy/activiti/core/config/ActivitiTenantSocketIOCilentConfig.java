package com.hhwy.activiti.core.config;

import com.hhwy.common.tenant.service.TenantSocketIOClientService;
import com.hhwy.common.tenant.service.TenantSocketIOServerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>描 述： DataSourceConfig
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:39
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Configuration
public class ActivitiTenantSocketIOCilentConfig {

    @Bean
    @ConditionalOnMissingBean(value = {TenantSocketIOServerService.class})
    public TenantSocketIOClientService tenantSocketIOClientService() {
        TenantSocketIOClientService tenantSocketIOClientService = new TenantSocketIOClientService("auto", false, false);
        return tenantSocketIOClientService;
    }

}
