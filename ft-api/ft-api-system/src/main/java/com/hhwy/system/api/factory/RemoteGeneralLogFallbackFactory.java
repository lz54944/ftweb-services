package com.hhwy.system.api.factory;

import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteGeneralLogService;
import com.hhwy.system.api.domain.SysGeneralLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 通用日志服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteGeneralLogFallbackFactory implements FallbackFactory<RemoteGeneralLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteGeneralLogFallbackFactory.class);

    @Override
    public RemoteGeneralLogService create(Throwable throwable) {
        log.error("通用日志服务调用失败:{}", throwable.getMessage());
        return new RemoteGeneralLogService() {
            @Override
            public R<Boolean> saveLog(SysGeneralLog sysGeneralLog) {
                return null;
            }
        };

    }
}
