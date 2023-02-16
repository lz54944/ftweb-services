package com.hhwy.common.tenant.runner;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.datasource.utils.DataSourceUtils;
import com.hhwy.common.tenant.config.TenantDataSourceConfig;
import org.springframework.boot.CommandLineRunner;
import java.net.URL;

public class ValidateRunner implements CommandLineRunner {

    private String genDbType;

    public ValidateRunner(String genDbType) {
        this.genDbType = genDbType;
    }

    @Override
    public void run(String... args) throws Exception {
        if (genDbType.equalsIgnoreCase("auto")) {
            String url = DataSourceUtils.getPrimaryDataSourceProperty().getUrl();
            if (!url.contains("nullCatalogMeansCurrent=true")) {
                throw new RuntimeException("请检查数据源url，确保设置了nullCatalogMeansCurrent=true的属性("+url+")");
            }
        } else if (genDbType.equalsIgnoreCase("script")) {
            URL resource = TenantDataSourceConfig.class.getResource(Constants.TENANT_DB_INIT_FILE);
            if (resource == null) {
                throw new RuntimeException("缺少数据库脚本("+Constants.TENANT_DB_INIT_FILE+")");
            }
        }else {
            throw new RuntimeException("project.tenant.genDbType 参数错误，仅支持‘auto’和‘script’类型");
        }
    }

}
