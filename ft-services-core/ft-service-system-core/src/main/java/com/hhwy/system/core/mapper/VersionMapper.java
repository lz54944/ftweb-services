package com.hhwy.system.core.mapper;

/**
 * 用户与角色关联表 数据层
 *
 * @author hhwy
 */
public interface VersionMapper {

    String selectVersion();

    int updateVersion(String version);

    int executeSql(String sql);

}
