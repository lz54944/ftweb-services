package com.hhwy.common.datasource.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * <br>描 述： jdbcUrl分解
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/2 14:17
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class JdbcUrlUtils {

    public static void main(String[] args) {
        JdbcDatabaseInfo jdbcDatabaseInfo = jdbcUrlSplitter("jdbc:mysql://192.168.1.66:3306/ry-cloud2?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        System.err.println(JSON.toJSONString(jdbcDatabaseInfo));
    }

    public static JdbcDatabaseInfo jdbcUrlSplitter(String jdbcUrl) {
        JdbcDatabaseInfo jdbcDatabaseInfo = new JdbcUrlUtils.JdbcDatabaseInfo();
        String connUri, driverName;
        String host = null;
        Integer port = null;
        String dbName = null;
        String params = null;
        int pos, pos1, pos2;
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:") || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        driverName = jdbcUrl.substring(5, pos1);
        if ((pos2 = jdbcUrl.indexOf(';', pos1)) == -1) {
            connUri = jdbcUrl.substring(pos1 + 1);
        } else {
            connUri = jdbcUrl.substring(pos1 + 1, pos2);
        }
        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                host = connUri.substring(2, pos);
                dbName = connUri.substring(pos + 1);
                if (dbName.contains("?")) {
                    dbName = dbName.substring(0, dbName.indexOf("?"));
                    params = connUri.substring(connUri.indexOf(dbName) + dbName.length() + 1);
                }
                if (dbName.contains(";")) {
                    dbName = dbName.substring(0, dbName.indexOf(";"));
                }
                if ((pos = host.indexOf(':')) != -1) {
                    port = Integer.parseInt(host.substring(pos + 1));
                    host = host.substring(0, pos);
                }
            }
        } else {
            dbName = connUri;
        }
        jdbcDatabaseInfo.setDbHost(host);
        jdbcDatabaseInfo.setDbPort(port);
        jdbcDatabaseInfo.setDbName(dbName);
        jdbcDatabaseInfo.setDbType(driverName);
        jdbcDatabaseInfo.setParams(params);
        return jdbcDatabaseInfo;
    }

    public static String jdbcUrlMysqlAssembly(String host, Integer port, String dbName, String params) {
        Assert.notNull(host, "database host is not allowed empty.");
        Assert.notNull(port, "database port is not allowed empty.");
        Assert.notNull(dbName, "database name is not allowed empty.");
        String url;
        if (StringUtils.isBlank(params)) {
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        } else {
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?" + params;
        }
        return url;
    }

    public static class JdbcDatabaseInfo {
        /**
         * 数据库地址
         */
        private String dbHost;

        /**
         * 数据库端口号
         */
        private Integer dbPort;

        /**
         * 数据库名称
         */
        private String dbName;

        /**
         * 数据库类型
         */
        private String dbType;

        String params;

        public String getDbHost() {
            return dbHost;
        }

        public void setDbHost(String dbHost) {
            this.dbHost = dbHost;
        }

        public Integer getDbPort() {
            return dbPort;
        }

        public void setDbPort(Integer dbPort) {
            this.dbPort = dbPort;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getDbType() {
            return dbType;
        }

        public void setDbType(String dbType) {
            this.dbType = dbType;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

}
