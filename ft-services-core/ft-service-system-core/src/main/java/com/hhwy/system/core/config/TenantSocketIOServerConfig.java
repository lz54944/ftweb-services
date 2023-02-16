package com.hhwy.system.core.config;

import com.hhwy.common.socket.config.SocketIOServerConfig;
import com.hhwy.common.tenant.config.TenantDataSourceConfig;
import com.hhwy.common.tenant.service.TenantSocketIOClientService;
import com.hhwy.common.tenant.service.TenantSocketIOServerService;
import com.hhwy.system.core.controller.SysTenantController;
import com.hhwy.system.core.service.impl.SysTenantServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <br>描 述： DataSourceConfig
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:39
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Import({SocketIOServerConfig.class/*, TenantDataSourceConfig.class*/})
@ConditionalOnMissingBean(TenantSocketIOClientService.class)//也可以通过 @EnableFtTenantServer手动开启  为了减少一个注解 默认开启了
@Configuration
public class TenantSocketIOServerConfig {

    @Bean
    public TenantSocketIOServerService tenantSocketIOServerService() {
        TenantSocketIOServerService tenantSocketIOServerService = new TenantSocketIOServerService();
        return tenantSocketIOServerService;
    }

    @Bean
    public SysTenantController sysTenantController() {
        SysTenantController sysTenantController = new SysTenantController();
        return sysTenantController;
    }

    @Bean
    public SysTenantServiceImpl sysTenantServiceImpl() {
        SysTenantServiceImpl sysTenantServiceImpl = new SysTenantServiceImpl();
        return sysTenantServiceImpl;
    }

}
