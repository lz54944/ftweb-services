package com.hhwy.common.tenant.config;

import com.hhwy.common.socket.core.ClientRunner;
import com.hhwy.common.socket.core.SocketIOClientHandler;
import com.hhwy.common.socket.service.ISocketIOClientService;
import com.hhwy.common.tenant.runner.TenantMultiDataSourceInitRunner;
import com.hhwy.common.tenant.runner.ValidateRunner;
import com.hhwy.common.tenant.service.TenantSocketIOClientService;
import com.hhwy.common.tenant.service.TenantSocketIOServerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * <br>描 述： DataSourceConfig
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:39
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Import(TenantDataSourceConfig.class)
@ConditionalOnMissingBean(TenantSocketIOServerService.class)
public class TenantSocketIOCilentConfig {

    @Value("${tenant.db.genType:auto}")
    private String genDbType;

    @Value("${tenant.db.dropIfExists:false}")
    private Boolean dropIfExists;

    @Bean
    @ConditionalOnMissingBean
    public TenantMultiDataSourceInitRunner tenantMultiDataSourceInitRunner() {
        TenantMultiDataSourceInitRunner tenantMultiDataSourceInitRunner = new TenantMultiDataSourceInitRunner();
        return tenantMultiDataSourceInitRunner;
    }

    @Bean
    @ConditionalOnMissingBean(value = {TenantSocketIOServerService.class})
    public ValidateRunner validateRunner() {
        ValidateRunner validateRunner = new ValidateRunner(genDbType);
        return validateRunner;
    }

    @Bean
    @ConditionalOnMissingBean(value = {TenantSocketIOServerService.class, ISocketIOClientService.class})
    public TenantSocketIOClientService tenantSocketIOClientService() {
        TenantSocketIOClientService tenantSocketIOClientService = new TenantSocketIOClientService(genDbType, dropIfExists);
        return tenantSocketIOClientService;
    }

    @Bean
    @ConditionalOnMissingBean(value = TenantSocketIOServerService.class)
    public ClientRunner clientRunner() {
        return new ClientRunner();
    }

    @Bean
    @ConditionalOnMissingBean(value = TenantSocketIOServerService.class)
    public SocketIOClientHandler socketIOClientHandler() {
        return new SocketIOClientHandler();
    }

}
