package com.hhwy.activiti.api.factory;

import com.hhwy.activiti.api.RemoteActivitiService;
import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 文件服务降级处理
 *
 * @author jzq
 */
@Component
public class RemoteActivitiFallbackFactory implements FallbackFactory<RemoteActivitiService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteActivitiFallbackFactory.class);

    @Override
    public RemoteActivitiService create(Throwable throwable) {
        log.error("流程服务调用失败:{}", throwable.getMessage());
        return new RemoteActivitiService() {
            @Override
            public R<ActiBusinessInfo> getInfo(String tenantKey, String businessTableName, String businessId) {
                return R.fail("获取数据失败:" + throwable.getMessage());
            }
        };
    }
}
