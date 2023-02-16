package com.hhwy.system.core.service;

import com.hhwy.system.api.domain.SysTenant;
import java.util.List;

/**
 * 租户管理Service接口
 * 
 * @author jzq
 * @date 2021-08-11
 */
public interface ISysTenantService {
    /**
     * 查询租户管理
     * 
     * @param tenantId 租户管理ID
     * @return 租户管理
     */
    SysTenant selectSysTenantById(Long tenantId);

    SysTenant selectSysTenantByTenantKey(String tenantKey);

    /**
     * 查询租户管理列表
     * 
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增租户管理
     * 
     * @param sysTenant 租户管理
     * @return 结果
     */
    int insertSysTenant(SysTenant sysTenant);

    /**
     * 修改租户管理
     * 
     * @param sysTenant 租户管理
     * @return 结果
     */
    int updateSysTenant(SysTenant sysTenant);

    /**
     * 批量删除租户管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysTenantByIds(String ids);

    /**
     * 删除租户管理信息
     * 
     * @param tenantId 租户管理ID
     * @return 结果
     */
    int deleteSysTenantById(Long tenantId);
}
