package com.hhwy.common.datasource.core;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.datasource.service.DynamicDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;

/**
 * <br>描 述： DynamicDataSource
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:21
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class DynamicDataSource extends DynamicRoutingDataSource{

    @Autowired
    DynamicDataSourceService dynamicDataSourceService;

    private static DynamicDataSourceProperties properties = SpringUtils.getBean(DynamicDataSourceProperties.class);

    //数据源动态切换核心
    @Override
    public DataSource determineDataSource() {
        //String dataSourceName = DynamicDataSourceContextHolder.peek();//使用@DS()注解，会调用DynamicDataSourceContextHolder.push()方法
        //return getDataSource(dataSourceName);
        return dynamicDataSourceService.determineDataSource(this);
    }
}
