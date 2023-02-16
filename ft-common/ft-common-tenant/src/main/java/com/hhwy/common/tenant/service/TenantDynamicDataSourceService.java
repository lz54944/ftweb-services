package com.hhwy.common.tenant.service;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.hhwy.common.core.utils.ServletUtils;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.common.tenant.utils.TenantDataSourceUtils;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * <br>描 述： DynamicDataSource
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:21
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class TenantDynamicDataSourceService extends DynamicRoutingDataSource {

    @Autowired
    TokenService tokenService;

    private static DynamicDataSourceProperties properties = SpringUtils.getBean(DynamicDataSourceProperties.class);

    //数据源动态切换核心
    @Override
    public DataSource determineDataSource() {
        String dataSourceName = DynamicDataSourceContextHolder.peek();//使用@DS()注解，会调用DynamicDataSourceContextHolder.push()方法
        if (StringUtils.isEmpty(dataSourceName)) {
            HttpServletRequest request = ServletUtils.getRequest();
            if(request != null){
                SysUser sysUser = tokenService.getSysUser();
                if(sysUser != null){
                    SysTenant tenant = sysUser.getTenant();
                    Assert.notNull(tenant,"未找到账号·"+sysUser.getUserName()+"·所属租户");
                    dataSourceName = TenantDataSourceUtils.getDataSourceNameByTenantKey(tenant.getTenantKey());
                }else {
                    // TODO: 2021/8/31 未登录

                }
            }
        }
        return getDataSource(dataSourceName);
    }
}
