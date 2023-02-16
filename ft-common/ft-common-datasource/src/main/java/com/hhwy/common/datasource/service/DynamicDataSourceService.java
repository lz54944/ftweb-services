package com.hhwy.common.datasource.service;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import javax.sql.DataSource;

public interface DynamicDataSourceService {
    DataSource determineDataSource(DynamicRoutingDataSource dynamicRoutingDataSource);
}
