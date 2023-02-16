package com.hhwy.system.core.runner;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.file.FileUtils;
import com.hhwy.system.core.mapper.VersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <br>描 述： 系统启动成功之后 执行须更新的SQL脚本
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/9/28 15:19
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Component
public class DbRunner implements CommandLineRunner {

    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private DataSource dataSource;

    private static final String sqlRootPath = "/sql";

    @Override
    public void run(String... args){
        try {
            URL resourceUrl = DbRunner.class.getResource(sqlRootPath);
            if (resourceUrl != null) {
                String protocol = resourceUrl.getProtocol();
                if ("jar".equalsIgnoreCase(protocol)) {
                    String jarVersion = getJarVersion();
                    String version = versionMapper.selectVersion();
                    if (!jarVersion.equals(version)) {
                        executeSqlScript(jarVersion);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getJarVersion(){
        String path = DbRunner.class.getResource(sqlRootPath).getPath();
        int lenth = (".jar!"+sqlRootPath).length();
        path = path.substring(0,path.length()-lenth);
        int index = path.lastIndexOf("-");
        String version = path.substring(index+1);
        return version;
    }

    public void executeSqlScript(String jarVersion) throws Exception{
        String resource = sqlRootPath+"/update-"+jarVersion+".sql";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            InputStream resourceAsStream = DbRunner.class.getResourceAsStream(resource);
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
                versionMapper.updateVersion(jarVersion);
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

}
