package com.hhwy.system.core.service.impl;

import java.util.List;

import com.hhwy.common.core.utils.DESUtil;
import com.hhwy.system.api.domain.SysTenantDb;
import com.hhwy.system.core.mapper.SysTenantDbMapper;
import com.hhwy.system.core.service.ISysTenantDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hhwy.common.core.text.Convert;

/**
 * 租户-数据库信息Service业务层处理
 * 
 * @author jzq
 * @date 2021-09-29
 */
@Service
public class SysTenantDbServiceImpl implements ISysTenantDbService {
    @Autowired
    private SysTenantDbMapper sysTenantDbMapper;

    /**
     * 查询租户-数据库信息
     * 
     * @param id 租户-数据库信息ID
     * @return 租户-数据库信息
     */
    @Override
    public SysTenantDb selectSysTenantDbById(Long id) {
        return sysTenantDbMapper.selectSysTenantDbById(id);
    }

    /**
     * 查询租户-数据库信息列表
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 租户-数据库信息
     */
    @Override
    public List<SysTenantDb> selectSysTenantDbList(SysTenantDb sysTenantDb) {
        return sysTenantDbMapper.selectSysTenantDbList(sysTenantDb);
    }

    @Override
    public List<SysTenantDb> getTenantDbByServiceName(String serviceName) {
        return sysTenantDbMapper.getTenantDbByServiceName(serviceName);
    }

    @Override
    public SysTenantDb getTenantDbByServiceNameAndTenantKey(String serviceName, String tenantKey) {
        return sysTenantDbMapper.getTenantDbByServiceNameAndTenantKey(serviceName,tenantKey);
    }

    /**
     * 新增租户-数据库信息
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 结果
     */
    @Override
    public int insertSysTenantDb(SysTenantDb sysTenantDb) {
        String encrypt = DESUtil.encrypt(sysTenantDb.getServiceName()+sysTenantDb.getTenantKey(), sysTenantDb.getDbPassword());
        sysTenantDb.setDbPassword(encrypt);
        return sysTenantDbMapper.insertSysTenantDb(sysTenantDb);
    }

    /**
     * 修改租户-数据库信息
     * 
     * @param sysTenantDb 租户-数据库信息
     * @return 结果
     */
    @Override
    public int updateSysTenantDb(SysTenantDb sysTenantDb) {
        return sysTenantDbMapper.updateSysTenantDb(sysTenantDb);
    }

    /**
     * 删除租户-数据库信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantDbByIds(String ids) {
        return sysTenantDbMapper.deleteSysTenantDbByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除租户-数据库信息信息
     * 
     * @param id 租户-数据库信息ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantDbById(Long id) {
        return sysTenantDbMapper.deleteSysTenantDbById(id);
    }
}
