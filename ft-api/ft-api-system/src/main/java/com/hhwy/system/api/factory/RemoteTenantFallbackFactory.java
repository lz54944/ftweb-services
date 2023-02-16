package com.hhwy.system.api.factory;

import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteTenantService;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysTenantDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 租户服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteTenantFallbackFactory implements FallbackFactory<RemoteTenantService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteTenantFallbackFactory.class);

    @Override
    public RemoteTenantService create(Throwable throwable) {
        log.error("租户服务调用失败:{}", throwable.getMessage());
        return new RemoteTenantService() {
            @Override
            public R<List<SysTenant>> getAllTenant() {
                return R.fail("获取全部租户信息失败:" + throwable.getMessage());
            }

            @Override
            public R<SysTenant> getTenantByTenantKey(String tenantKey) {
                return R.fail("获取租户信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysTenantDb>> getTenantDbByServiceName(String serviceName) {
                return R.fail("获取全部租户数据库信息失败:" + throwable.getMessage());
            }

            @Override
            public R<SysTenantDb> getTenantDbByServiceNameAndTenantKey(String serviceName, String tenantKey) {
                return R.fail("获取租户数据库信息失败:" + throwable.getMessage());
            }

            @Override
            public R addSysTenantDb(SysTenantDb sysTenantDb) {
                return R.fail("添加租户数据库信息失败:" + throwable.getMessage());
            }

            @Override
            public R editSysTenantDb(SysTenantDb sysTenantDb) {
                return R.fail("修改租户数据库信息失败:" + throwable.getMessage());
            }
        };
    }
}
