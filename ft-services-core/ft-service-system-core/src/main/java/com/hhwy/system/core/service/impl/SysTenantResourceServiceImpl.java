package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.text.Convert;
import com.hhwy.system.core.domain.SysTenantResource;
import com.hhwy.system.core.mapper.SysTenantResourceMapper;
import com.hhwy.system.core.service.ISysTenantResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户-资源映射关系Service业务层处理
 * 
 * @author jzq
 * @date 2021-08-11
 */
@Service
public class SysTenantResourceServiceImpl implements ISysTenantResourceService {

    @Autowired
    private SysTenantResourceMapper sysTenantResourceMapper;

    /**
     * 查询租户-资源映射关系
     * 
     * @param id 租户-资源映射关系ID
     * @return 租户-资源映射关系
     */
    @Override
    public SysTenantResource selectSysTenantResourceById(Long id) {
        return sysTenantResourceMapper.selectSysTenantResourceById(id);
    }

    /**
     * 查询租户-资源映射关系列表
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 租户-资源映射关系
     */
    @Override
    public List<SysTenantResource> selectSysTenantResourceList(SysTenantResource sysTenantResource) {
        return sysTenantResourceMapper.selectSysTenantResourceList(sysTenantResource);
    }

    /**
     * 新增租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    @Override
    public int insertSysTenantResource(SysTenantResource sysTenantResource) {
        return sysTenantResourceMapper.insertSysTenantResource(sysTenantResource);
    }

    /**
     * 修改租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    @Override
    public int updateSysTenantResource(SysTenantResource sysTenantResource) {
        return sysTenantResourceMapper.updateSysTenantResource(sysTenantResource);
    }

    /**
     * 删除租户-资源映射关系对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantResourceByIds(String ids) {
        return sysTenantResourceMapper.deleteSysTenantResourceByIdArr(Convert.toStrArray(ids));
    }

    /**
     * 删除租户-资源映射关系信息
     * 
     * @param id 租户-资源映射关系ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantResourceById(Long id) {
        return sysTenantResourceMapper.deleteSysTenantResourceById(id);
    }
}
