package com.hhwy.system.api;

import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.domain.SysMenu;
import com.hhwy.system.api.factory.RemoteMenuFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 *
 * @author jzq
 */
@FeignClient(contextId = "remoteMenuService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface RemoteMenuService {

    /**
     * 新增菜单
     */
    @PostMapping(value = "/menu")
    R add(@Validated @RequestBody SysMenu menu);

    /**
     * 修改菜单
     */
    @PutMapping(value = "/menu")
    R edit(@Validated @RequestBody SysMenu menu);

}
