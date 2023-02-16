package com.hhwy.auth.api.factory;

import com.hhwy.auth.api.RemoteTokenService;
import com.hhwy.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 用户服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteTokenFallbackFactory implements FallbackFactory<RemoteTokenService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteTokenFallbackFactory.class);

    @Override
    public RemoteTokenService create(Throwable throwable) {
        log.error("Auth服务调用失败:{}", throwable.getMessage());
        return new RemoteTokenService() {
            @Override
            public R<Map<String, Object>> ssoValidate(Map<String, String> params) {
                return R.fail("认证失败:" + throwable.getMessage());
            }
        };
    }
}
