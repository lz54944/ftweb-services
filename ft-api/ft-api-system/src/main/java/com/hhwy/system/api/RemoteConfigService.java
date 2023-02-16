package com.hhwy.system.api;

import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务
 *
 * @author hhwy
 */
@FeignClient(contextId = "remoteConfigService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteConfigService {
    /**
     * 根据参数键名查询参数值
     *
     * @param configKey key
     * @return 结果
     */
    @GetMapping(value = "/config/configKey/{configKey}")
    R<String> getConfigKey(@PathVariable("configKey") String configKey) ;
}
