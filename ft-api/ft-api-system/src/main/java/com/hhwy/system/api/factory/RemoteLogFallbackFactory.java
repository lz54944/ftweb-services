package com.hhwy.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteLogService;
import com.hhwy.system.api.domain.SysOperLog;

/**
 * 日志服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog) {
                return null;
            }

            @Override
            public R<Boolean> saveLogininfor(String tenantKey, String username, String status, String message) {
                return null;
            }
        };

    }
}
