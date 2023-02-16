package com.hhwy.system.api.factory;

import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteMenuService;
import com.hhwy.system.api.domain.SysMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 菜单服务降级处理
 *
 * @author jzq
 */
@Component
public class RemoteMenuFallbackFactory implements FallbackFactory<RemoteMenuService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMenuFallbackFactory.class);

    @Override
    public RemoteMenuService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteMenuService() {
            @Override
            public R add(SysMenu menu) {
                return R.fail("添加菜单失败:" + throwable.getMessage());
            }

            @Override
            public R edit(SysMenu menu) {
                return R.fail("修改菜单失败:" + throwable.getMessage());
            }
        };
    }
}
