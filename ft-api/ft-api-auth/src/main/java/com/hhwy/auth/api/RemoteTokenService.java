package com.hhwy.auth.api;

import com.hhwy.auth.api.factory.RemoteTokenFallbackFactory;
import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * 用户服务
 *
 * @author hhwy
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE, fallbackFactory = RemoteTokenFallbackFactory.class)
public interface RemoteTokenService {

    /**
     * 单点认证
     * 接收参数: tenantKey：租户标识、token：加密串、key：加密key
     */
    @PostMapping(value = "/sso/validate")
    R<Map<String, Object>> ssoValidate(@RequestBody Map<String,String> params) ;
}
