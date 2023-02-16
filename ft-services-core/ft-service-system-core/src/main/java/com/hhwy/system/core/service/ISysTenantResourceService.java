package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysTenantResource;
import java.util.List;

/**
 * 租户-资源映射关系Service接口
 * 
 * @author jzq
 * @date 2021-08-11
 */
public interface ISysTenantResourceService {
    /**
     * 查询租户-资源映射关系
     * 
     * @param id 租户-资源映射关系ID
     * @return 租户-资源映射关系
     */
    SysTenantResource selectSysTenantResourceById(Long id);

    /**
     * 查询租户-资源映射关系列表
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 租户-资源映射关系集合
     */
    List<SysTenantResource> selectSysTenantResourceList(SysTenantResource sysTenantResource);

    /**
     * 新增租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    int insertSysTenantResource(SysTenantResource sysTenantResource);

    /**
     * 修改租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    int updateSysTenantResource(SysTenantResource sysTenantResource);

    /**
     * 批量删除租户-资源映射关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysTenantResourceByIds(String ids);

    /**
     * 删除租户-资源映射关系信息
     * 
     * @param id 租户-资源映射关系ID
     * @return 结果
     */
    int deleteSysTenantResourceById(Long id);
}
