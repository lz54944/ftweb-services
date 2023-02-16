package com.hhwy.common.tenant.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.tenant.service.TenantDynamicDataSourceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;

/**
 * <br>描 述： DataSourceConfig
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:39
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class TenantDataSourceConfig {

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProperties properties) {
        TenantDynamicDataSourceService dataSource = new TenantDynamicDataSourceService();
        dataSource.setPrimary(Constants.MASTER_TENANT_KEY);
        dataSource.setStrict(true);
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

}
