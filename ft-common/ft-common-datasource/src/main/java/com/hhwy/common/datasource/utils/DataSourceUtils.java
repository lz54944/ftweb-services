package com.hhwy.common.datasource.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.utils.DESUtil;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.bean.BeanUtils;
import com.hhwy.common.core.utils.file.FileUtils;
import com.hhwy.common.core.utils.sql.SqlUtils;
import com.hhwy.system.api.domain.SysTenantDb;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： DatasourceUtils
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/11 13:46
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class DataSourceUtils {
    private static String resource = Constants.TENANT_DB_INIT_FILE;

    private static DynamicDataSourceProperties properties = SpringUtils.getBean(DynamicDataSourceProperties.class);

    private static DynamicRoutingDataSource dynamicDataSource = SpringUtils.getBean(DynamicRoutingDataSource.class);

    private static DefaultDataSourceCreator defaultDataSourceCreator = SpringUtils.getBean(DefaultDataSourceCreator.class);

    public static DataSourceProperty getPrimaryDataSourceProperty() {
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasource();
        String primary = properties.getPrimary();
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String key = item.getKey();
            if(primary.equals(key)){
                return item.getValue();
            }
        }
        throw new RuntimeException("请确保系统中已设置了"+Constants.MASTER_TENANT_KEY+"数据源！");
    }

    public static void genDataSource(String dataSourceName, String genDbType){
        try {
            Map<String, DataSource> currentDataSources = dynamicDataSource.getCurrentDataSources();
            boolean exist = currentDataSources.containsKey(dataSourceName);
            if(exist){
                throw new RuntimeException(dataSourceName+"数据源已存在！");
            }

            //创建数据库
            createDataBase(dynamicDataSource, dataSourceName);

            DataSourceProperty primaryDataSourceProperty = getPrimaryDataSourceProperty();
            DataSourceProperty newDataSourceProperty = new DataSourceProperty();
            BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);
            String url = newDataSourceProperty.getUrl();
            JdbcUrlUtils.JdbcDatabaseInfo jdbcDatabaseInfo = JdbcUrlUtils.jdbcUrlSplitter(url);

            newDataSourceProperty.setPoolName(dataSourceName);
            //newDataSourceProperty.setUrl("jdbc:mysql://192.168.1.66:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
            newDataSourceProperty.setUrl("jdbc:mysql://"+jdbcDatabaseInfo.getDbHost()+":"+jdbcDatabaseInfo.getDbPort()+"/"+dataSourceName+"?"+jdbcDatabaseInfo.getParams());

            DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
            dynamicDataSource.addDataSource(dataSourceName, dataSource);

            // 创建表
            if (genDbType.equalsIgnoreCase("auto")) {
                List<String> tableNameList = SqlUtils.getTableNames(dynamicDataSource.getConnection());
                List<String> createTablesSqlList = SqlUtils.getCreateTablesSql(dynamicDataSource.getConnection(),tableNameList);
                createTablesBySqls(dynamicDataSource.getDataSource(dataSourceName),createTablesSqlList);

            }else if (genDbType.equalsIgnoreCase("script")) {
                createTablesBySqlScript(dynamicDataSource.getDataSource(dataSourceName));
            }else {
                throw new RuntimeException("genDbType 参数错误，仅支持‘auto’和‘script’类型");
            }
            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            e.printStackTrace();
        }
    }

    public static void addDataSource(String dataSourceName){
        try {
            Map<String, DataSource> currentDataSources = dynamicDataSource.getCurrentDataSources();
            boolean exist = currentDataSources.containsKey(dataSourceName);
            if(exist){
                throw new RuntimeException(dataSourceName+"数据源已存在！");
            }

            DataSourceProperty primaryDataSourceProperty = getPrimaryDataSourceProperty();
            DataSourceProperty newDataSourceProperty = new DataSourceProperty();
            BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);
            String url = newDataSourceProperty.getUrl();
            JdbcUrlUtils.JdbcDatabaseInfo jdbcDatabaseInfo = JdbcUrlUtils.jdbcUrlSplitter(url);

            newDataSourceProperty.setPoolName(dataSourceName);
            newDataSourceProperty.setUrl("jdbc:mysql://"+jdbcDatabaseInfo.getDbHost()+":"+jdbcDatabaseInfo.getDbPort()+"/"+dataSourceName+"?"+jdbcDatabaseInfo.getParams());

            DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
            addDataSource(dataSourceName, dataSource);

            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            throw new RuntimeException(e);
        }
    }

    public static void addDataSource(String dataSourceName, DataSource dataSource){
        try {
            dynamicDataSource.addDataSource(dataSourceName, dataSource);
            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            throw new RuntimeException(e);
        }
    }

    public static void addDataSource(SysTenantDb sysTenantDb){
        String dataSourceName = sysTenantDb.getDbName();
        try {
            DataSourceProperty primaryDataSourceProperty = getPrimaryDataSourceProperty();
            DataSourceProperty newDataSourceProperty = new DataSourceProperty();
            BeanUtils.copyBeanProp(newDataSourceProperty,primaryDataSourceProperty);

            newDataSourceProperty.setPoolName(dataSourceName);
            newDataSourceProperty.setUrl(sysTenantDb.getDbUrl());
            newDataSourceProperty.setPassword(DESUtil.decrypt(sysTenantDb.getServiceName()+sysTenantDb.getTenantKey(), sysTenantDb.getDbPassword()));
            newDataSourceProperty.setUsername(sysTenantDb.getDbUserName());

            DataSource dataSource = defaultDataSourceCreator.createDataSource(newDataSourceProperty);
            dynamicDataSource.addDataSource(dataSourceName, dataSource);
            System.err.println("*******************数据源·"+dataSourceName+"·添加成功*******************");
        } catch (Exception e) {
            System.err.println("*******************数据源·"+dataSourceName+"·添加失败*******************");
            throw new RuntimeException(e);
        }
    }

    public static void removeDataSource(String dataSourceName){
        //dynamicDataSource.removeDataSource(dataSourceName);// TODO: 2021/9/23  此行代码会报错  但是 确实删除了

        Map<String, DataSource> currentDataSources = dynamicDataSource.getCurrentDataSources();
        if (currentDataSources.containsKey(dataSourceName)) {
            currentDataSources.remove(dataSourceName);
        }
    }

    public static void createDataBase(DynamicRoutingDataSource dataSource,String dataBaseName) throws Exception{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS "+dataBaseName+" default charset utf8 COLLATE utf8_general_ci;");
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteDataBase(DynamicRoutingDataSource dataSource,String dataBaseName) throws Exception{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute("DROP DATABASE "+dataBaseName);
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createTablesBySqlScript(DataSource dataSource) throws Exception{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            InputStream resourceAsStream = DataSourceUtils.class.getResourceAsStream(resource);
            String sql = FileUtils.readFile(resourceAsStream);
            if (StringUtils.isNotEmpty(sql)) {
                String[] sqlArr = sql.split(";");
                for (String sqlStr : sqlArr) {
                    if(StringUtils.isNotEmpty(sqlStr)){
                        statement.addBatch(sqlStr);
                    }
                }
                statement.executeBatch();
                connection.commit();
            }
        }catch (Exception e){
            connection.rollback();
            connection.setAutoCommit(true);
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createTablesBySqls(DataSource dataSource, List<String> createTablesSqlList) throws Exception{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            for (String sql : createTablesSqlList) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkDbIsExists(DynamicRoutingDataSource dataSource,String dataBaseName) throws Exception{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT information_schema.SCHEMATA.SCHEMA_NAME FROM information_schema.SCHEMATA where SCHEMA_NAME = '" + dataBaseName + "'");
            return resultSet.next();
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}