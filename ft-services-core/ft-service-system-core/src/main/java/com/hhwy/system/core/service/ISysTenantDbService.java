package com.hhwy.system.core.service;

import com.hhwy.system.api.domain.SysTenantDb;

import java.util.List;

/**
 * 租户-数据库信息Service接口
 * 
 * @author jzq
 * @date 2021-09-29
 */
public interface ISysTenantDbService {
    /**
     * 查询租户-数据库信息
     * 
     * @param id 租户-数据库信息ID
     * @return 租户-数据库信息
     */
    SysTenantDb selectSysTenantDbById(Long id);

    /**
     * 查询租户-数据库信息列表
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 租户-数据库信息集合
     */
    List<SysTenantDb> selectSysTenantDbList(SysTenantDb sysTenantDb);

    List<SysTenantDb> getTenantDbByServiceName(String serviceName) ;

    SysTenantDb getTenantDbByServiceNameAndTenantKey(String serviceName,String tenantKey);

    /**
     * 新增租户-数据库信息
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 结果
     */
    int insertSysTenantDb(SysTenantDb sysTenantDb);

    /**
     * 修改租户-数据库信息
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 结果
     */
    int updateSysTenantDb(SysTenantDb sysTenantDb);

    /**
     * 批量删除租户-数据库信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysTenantDbByIds(String ids);

    /**
     * 删除租户-数据库信息信息
     * 
     * @param id 租户-数据库信息ID
     * @return 结果
     */
    int deleteSysTenantDbById(Long id);
}
