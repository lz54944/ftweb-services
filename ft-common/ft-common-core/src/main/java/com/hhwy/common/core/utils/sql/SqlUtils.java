package com.hhwy.common.core.utils.sql;

import com.hhwy.common.core.exception.BaseException;
import com.hhwy.common.core.utils.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sql操作工具类
 *
 * @author hhwy
 */
public class SqlUtils {
    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new BaseException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    public static List<String> getTableNames(Connection conn) {
        List<String> tableNames = new ArrayList<>();
        ResultSet rs = null;
        try {
            // 获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            // 从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] {"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("getTableNames failure");
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return tableNames;
    }

    public static String getCreateTableSql(Connection conn, String tableName) {
        String sql = String.format("SHOW CREATE TABLE %s", tableName);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // 返回建表语句语句，查询结果的第二列是建表语句，第一列是表名
                return rs.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != pstmt) {
                try {
                    pstmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    public static List<String> getCreateTablesSql(Connection conn, List<String> tableNameList){
        List<String> createTablesSqlList = new ArrayList<>();
        PreparedStatement pstmt = null;
        try {
            for (String tableName : tableNameList) {
                String sql = String.format("SHOW CREATE TABLE %s", tableName);
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    // 返回建表语句语句，查询结果的第二列是建表语句，第一列是表名
                    String createTablesSql = rs.getString(2);
                    createTablesSqlList.add(createTablesSql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != pstmt) {
                try {
                    pstmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return createTablesSqlList;
    }
}
