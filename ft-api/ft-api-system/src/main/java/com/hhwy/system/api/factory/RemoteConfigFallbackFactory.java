package com.hhwy.system.api.factory;

import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteConfigFallbackFactory implements FallbackFactory<RemoteConfigService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteConfigFallbackFactory.class);

    @Override
    public RemoteConfigService create(Throwable throwable) {
        log.error("系统服务调用失败:{}", throwable.getMessage());
        return new RemoteConfigService() {
            @Override
            public R<String> getConfigKey(String configKey) {
                return R.fail("获取配置信息失败:" + throwable.getMessage());
            }
        };
    }
}
