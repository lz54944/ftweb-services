package com.hhwy.system.api;

import com.hhwy.common.core.constant.ServiceNameConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysTenantDb;
import com.hhwy.system.api.factory.RemoteTenantFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户服务
 *
 * @author hhwy
 */
@FeignClient(contextId = "remoteTenantService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteTenantFallbackFactory.class)
public interface RemoteTenantService {

    /**
     * 查询全部的租户信息
     *
     * @return 结果
     */
    @GetMapping(value = "/tenant/getAllTenant")
    R<List<SysTenant>> getAllTenant();

    @GetMapping(value = "/tenant/getTenantByTenantKey")
    R<SysTenant> getTenantByTenantKey(@RequestParam("tenantKey") String tenantKey);

    /**
     * 查询全部的租户数据库信息
     *
     * @return 结果
     */
    @GetMapping(value = "/tenant/db/getTenantDbByServiceName")
    R<List<SysTenantDb>> getTenantDbByServiceName(@RequestParam("serviceName") String serviceName);

    @GetMapping(value = "/tenant/db/getTenantDbByServiceNameAndTenantKey")
    R<SysTenantDb> getTenantDbByServiceNameAndTenantKey(@RequestParam("serviceName") String serviceName, @RequestParam("tenantKey") String tenantKey);

    /**
     * 添加租户信息
     *
     * @return 结果
     */
    @PostMapping(value = "/tenant/db")
    R addSysTenantDb(@RequestBody SysTenantDb sysTenantDb);

    /**
     * 添加租户信息
     *
     * @return 结果
     */
    @PutMapping(value = "/tenant/db")
    R editSysTenantDb(@RequestBody SysTenantDb sysTenantDb);

}