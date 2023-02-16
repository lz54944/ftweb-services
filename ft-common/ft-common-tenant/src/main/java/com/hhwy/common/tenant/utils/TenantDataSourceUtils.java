package com.hhwy.common.tenant.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.bean.BeanUtils;
import com.hhwy.common.core.utils.sql.SqlUtils;
import com.hhwy.common.datasource.utils.DataSourceUtils;
import com.hhwy.common.datasource.utils.JdbcUrlUtils;
import com.hhwy.system.api.RemoteTenantService;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysTenantDb;
import org.springframework.util.Assert;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import static com.hhwy.common.datasource.utils.DataSourceUtils.createDataBase;

/**
 * <br>描 述： DatasourceUtils
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/11 13:46
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class TenantDataSourceUtils {

    private static DynamicRoutingDataSource dynamicDataSource = SpringUtils.getBean(DynamicRoutingDataSource.class);

    private static DefaultDataSourceCreator defaultDataSourceCreator = SpringUtils.getBean(DefaultDataSourceCreator.class);

    private static RemoteTenantService remoteTenantService = SpringUtils.getBean(RemoteTenantService.class);


    public static void genTenantDataSource(SysTenant sysTenant, String genDbType, Boolean dropIfExists){
        String dataSourceName = getDataSourceNameByTenantKey(sysTenant.getTenantKey());
        try {
            Map<String, DataSource> currentDataSources = dynamicDataSource.getCurrentDataSources();
            boolean existInContent = currentDataSources.containsKey(dataSourceName);
            if(existInContent){
                if (dropIfExists) {
                    DataSourceUtils.removeDataSource(dataSourceName);
                }else {
                    throw new RuntimeException(dataSourceName+"数据源已存在，不可重名！");
                }
            }

            boolean existInDb = DataSourceUtils.checkDbIsExists(dynamicDataSource, dataSourceName);
            if(existInDb){
                if (dropIfExists) {
                    DataSourceUtils.deleteDataBase(dynamicDataSource, dataSourceName);
                }else {
                    throw new RuntimeException(dataSourceName+"数据库已存在,请先去删除！");
                }
            }

            //创建数据库
            createDataBase(dynamicDataSource, dataSourceName);

            DataSourceProperty primaryDataSourceProperty = DataSourceUtils.getPrimaryDataSourceProperty();
            DataSourceProperty newDataSourceProperty = new DataSourceProperty();
            BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);
            String url = newDataSourceProperty.getUrl();
            JdbcUrlUtils.JdbcDatabaseInfo jdbcDatabaseInfo = JdbcUrlUtils.jdbcUrlSplitter(url);

            String dbType = jdbcDatabaseInfo.getDbType();
            String dbHost = jdbcDatabaseInfo.getDbHost();
            Integer dbPort = jdbcDatabaseInfo.getDbPort();
            String dbUrl = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dataSourceName+"?"+jdbcDatabaseInfo.getParams();

            newDataSourceProperty.setPoolName(dataSourceName);
            //newDataSourceProperty.setUrl("jdbc:mysql://192.168.1.66:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
            newDataSourceProperty.setUrl(dbUrl);

            DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
            dynamicDataSource.addDataSource(dataSourceName, dataSource);

            // 创建表
            if (genDbType.equalsIgnoreCase("auto")) {
                List<String> tableNameList = SqlUtils.getTableNames(dynamicDataSource.getConnection());
                List<String> createTablesSqlList = SqlUtils.getCreateTablesSql(dynamicDataSource.getConnection(),tableNameList);
                DataSourceUtils.createTablesBySqls(dynamicDataSource.getDataSource(dataSourceName),createTablesSqlList);

            }else if (genDbType.equalsIgnoreCase("script")) {
                DataSourceUtils.createTablesBySqlScript(dynamicDataSource.getDataSource(dataSourceName));
            }else {
                throw new RuntimeException("tenant.db.genType 参数错误，仅支持‘auto’和‘script’类型");
            }
            if(!existInContent){
                // 添加租户数据库信息
                addTenantDb(sysTenant.getTenantKey(), dbHost, dataSourceName, dbPort, dbType, primaryDataSourceProperty.getUsername(), primaryDataSourceProperty.getPassword(),dbUrl);
            }

            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            e.printStackTrace();
        }
    }

    //改方法已闲置  不必纠结
    public static void editTenantDataSource(SysTenant sysTenant, String genDbType, Boolean dropIfExists){
        String dataSourceName = getDataSourceNameByTenantKey(sysTenant.getTenantKey());
        try {
            Map<String, DataSource> currentDataSources = dynamicDataSource.getCurrentDataSources();
            boolean existInContent = currentDataSources.containsKey(dataSourceName);
            if(existInContent){
                if (dropIfExists) {
                    DataSourceUtils.removeDataSource(dataSourceName);
                }else {
                    System.err.println("*******************数据源·"+dataSourceName+"·已存在,如果想重置,请将 tenant.db.dropIfExists 设置为true*******************");
                    return;
                }
            }

            boolean existInDb = DataSourceUtils.checkDbIsExists(dynamicDataSource, dataSourceName);
            if(existInDb){
                if (dropIfExists) {
                    DataSourceUtils.deleteDataBase(dynamicDataSource, dataSourceName);
                }else {
                    addDataSourceToContent(dataSourceName);
                    System.err.println("*******************数据源·"+dataSourceName+"·已存在,如果想重置,请将 tenant.db.dropIfExists 设置为true*******************");
                    return;
                }
            }

            //创建数据库
            createDataBase(dynamicDataSource, dataSourceName);

            DataSourceProperty primaryDataSourceProperty = DataSourceUtils.getPrimaryDataSourceProperty();
            DataSourceProperty newDataSourceProperty = new DataSourceProperty();
            BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);
            String url = newDataSourceProperty.getUrl();
            JdbcUrlUtils.JdbcDatabaseInfo jdbcDatabaseInfo = JdbcUrlUtils.jdbcUrlSplitter(url);

            String dbType = jdbcDatabaseInfo.getDbType();
            String dbHost = jdbcDatabaseInfo.getDbHost();
            Integer dbPort = jdbcDatabaseInfo.getDbPort();
            String dbUrl = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dataSourceName+"?"+jdbcDatabaseInfo.getParams();

            newDataSourceProperty.setPoolName(dataSourceName);
            //newDataSourceProperty.setUrl("jdbc:mysql://192.168.1.66:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
            newDataSourceProperty.setUrl(dbUrl);

            DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
            dynamicDataSource.addDataSource(dataSourceName, dataSource);

            // 创建表
            if (genDbType.equalsIgnoreCase("auto")) {
                List<String> tableNameList = SqlUtils.getTableNames(dynamicDataSource.getConnection());
                List<String> createTablesSqlList = SqlUtils.getCreateTablesSql(dynamicDataSource.getConnection(),tableNameList);
                DataSourceUtils.createTablesBySqls(dynamicDataSource.getDataSource(dataSourceName),createTablesSqlList);
            }else if (genDbType.equalsIgnoreCase("script")) {
                DataSourceUtils.createTablesBySqlScript(dynamicDataSource.getDataSource(dataSourceName));
            }else {
                throw new RuntimeException("tenant.db.genType 参数错误，仅支持‘auto’和‘script’类型");
            }
            if(!existInContent){
                String serviceName = SpringUtils.getApplicationName();
                SysTenantDb tenantDb = remoteTenantService.getTenantDbByServiceNameAndTenantKey(serviceName, sysTenant.getTenantKey()).getData();
                if (tenantDb == null) {
                    // 添加租户数据库信息
                    addTenantDb(sysTenant.getTenantKey(), dbHost, dataSourceName, dbPort, dbType, primaryDataSourceProperty.getUsername(), primaryDataSourceProperty.getPassword(),dbUrl);
                }else {
                    tenantDb.setDbHost(dbHost);
                    tenantDb.setDbName(dataSourceName);
                    tenantDb.setDbPort(dbPort);
                    tenantDb.setDbType(dbType);
                    tenantDb.setDbUserName(primaryDataSourceProperty.getUsername());
                    tenantDb.setDbPassword(primaryDataSourceProperty.getPassword());
                    tenantDb.setDbUrl(dbUrl);
                    remoteTenantService.editSysTenantDb(tenantDb);
                }
            }

            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            e.printStackTrace();
        }
    }

    //tenantKey 转 dataSourceName
    public static String getDataSourceNameByTenantKey(String tenantKey){
        Assert.notNull(tenantKey,"租户key不能为空");
        if (Constants.MASTER_TENANT_KEY.equalsIgnoreCase(tenantKey)){
            return tenantKey.toLowerCase();
        }
        String serviceName = SpringUtils.getApplicationName();
        String dataSourceName = serviceName+"_"+tenantKey;
        return dataSourceName.replace("-","_").toLowerCase();
    }

    public static void addDataSourceToContent(String dataSourceName){
        DataSourceProperty primaryDataSourceProperty = DataSourceUtils.getPrimaryDataSourceProperty();
        DataSourceProperty newDataSourceProperty = new DataSourceProperty();
        BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);
        String url = newDataSourceProperty.getUrl();
        JdbcUrlUtils.JdbcDatabaseInfo jdbcDatabaseInfo = JdbcUrlUtils.jdbcUrlSplitter(url);

        String dbHost = jdbcDatabaseInfo.getDbHost();
        Integer dbPort = jdbcDatabaseInfo.getDbPort();

        newDataSourceProperty.setPoolName(dataSourceName);
        //newDataSourceProperty.setUrl("jdbc:mysql://192.168.1.66:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        newDataSourceProperty.setUrl("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dataSourceName+"?"+jdbcDatabaseInfo.getParams());

        DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
        dynamicDataSource.addDataSource(dataSourceName, dataSource);
    }

    public static void addTenantDb(String tenantKey, String dbHost, String dbName,Integer dbPort, String dbType, String dbUserName, String dbPassword, String dbUrl){
        String serviceName = SpringUtils.getApplicationName();
        SysTenantDb sysTenantDb = new SysTenantDb();
        sysTenantDb.setTenantKey(tenantKey);
        sysTenantDb.setServiceName(serviceName);
        sysTenantDb.setDbHost(dbHost);
        sysTenantDb.setDbName(dbName);
        sysTenantDb.setDbPort(dbPort);
        sysTenantDb.setDbType(dbType);
        sysTenantDb.setDbUserName(dbUserName);
        sysTenantDb.setDbPassword(dbPassword);
        sysTenantDb.setDbUrl(dbUrl);
        remoteTenantService.addSysTenantDb(sysTenantDb);
    }

}