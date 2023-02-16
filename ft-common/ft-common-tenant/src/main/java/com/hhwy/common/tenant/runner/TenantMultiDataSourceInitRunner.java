package com.hhwy.common.tenant.runner;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.datasource.utils.DataSourceUtils;
import com.hhwy.common.tenant.service.TenantSocketIOClientService;
import com.hhwy.common.tenant.utils.TenantDataSourceUtils;
import com.hhwy.system.api.RemoteTenantService;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysTenantDb;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： 初始化多数据源
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/9/28 15:30
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class TenantMultiDataSourceInitRunner implements CommandLineRunner {

    @Value("${tenant.db.genType:auto}")
    private String genDbType;

    @Value("${tenant.db.dropIfExists:false}")
    private Boolean dropIfExists;

    @Autowired
    RemoteTenantService remoteTenantService;

    @Autowired
    TenantSocketIOClientService tenantSocketIOClientService;

    @Override
    public void run(String... args) throws Exception {
        if (tenantSocketIOClientService.getNeedCreateDb()) {
            R<List<SysTenant>> allTenant = remoteTenantService.getAllTenant();
            if (Constants.FAIL == allTenant.getCode()) {
                throw new RuntimeException("获取全部租户信息异常,请检查system服务是否正常！");
            }

            List<SysTenant> sysTenantList = allTenant.getData();
            if (CollectionUtils.isEmpty(sysTenantList)) {
                return;
            }

            String serviceName = SpringUtils.getApplicationName();
            R<List<SysTenantDb>> allTenantDb = remoteTenantService.getTenantDbByServiceName(serviceName);//已创建的db
            if (Constants.FAIL == allTenantDb.getCode()) {
                throw new RuntimeException("获取全部租户数据库信息异常");
            }

            List<SysTenantDb> sysTenantDbList = allTenantDb.getData();
            Map<String,SysTenantDb> sysTenantDbMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(sysTenantDbList)) {
                for (SysTenantDb sysTenantDb : sysTenantDbList) {
                    sysTenantDbMap.put(sysTenantDb.getTenantKey(),sysTenantDb);
                }
            }

            Iterator<SysTenant> sysTenantIterator = sysTenantList.iterator();
            while (sysTenantIterator.hasNext()) {
                SysTenant sysTenant = sysTenantIterator.next();
                SysTenantDb sysTenantDb = sysTenantDbMap.get(sysTenant.getTenantKey());
                if (sysTenantDb != null) {
                    DataSourceUtils.addDataSource(sysTenantDb);
                }else {
                    TenantDataSourceUtils.genTenantDataSource(sysTenant, genDbType, dropIfExists);
                }
            }
        }
    }
}
