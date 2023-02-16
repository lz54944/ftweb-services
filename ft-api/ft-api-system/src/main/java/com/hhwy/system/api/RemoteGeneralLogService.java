package com.hhwy.system.api;

import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.domain.SysGeneralLog;
import com.hhwy.system.api.factory.RemoteGeneralLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 日志服务
 *
 * @author hhwy
 */
@FeignClient(contextId = "remoteGeneralLogService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteGeneralLogFallbackFactory.class)
public interface RemoteGeneralLogService {
    /**
     * 保存日志
     *
     * @param sysGeneralLog 日志实体
     * @return 结果
     */
    @PostMapping("/generalLog")
    R<Boolean> saveLog(@RequestBody SysGeneralLog sysGeneralLog);

}
