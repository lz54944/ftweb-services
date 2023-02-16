package com.hhwy.system.core.mapper;

import com.hhwy.system.api.domain.*;
import com.hhwy.system.core.domain.SysTenantResource;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 租户-资源映射关系Mapper接口
 * 
 * @author jzq
 * @date 2021-08-11
 */
public interface SysTenantResourceMapper {
    /**
     * 查询租户-资源映射关系
     * 
     * @param id 租户-资源映射关系ID
     * @return 租户-资源映射关系
     */
    SysTenantResource selectSysTenantResourceById(Long id);

    /**
     * 查询租户-资源映射关系列表
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 租户-资源映射关系集合
     */
    List<SysTenantResource> selectSysTenantResourceList(SysTenantResource sysTenantResource);

    List<String> selectResourceIdByResourceTableAndOriginalIdList(@Param("tenantKey") String tenantKey, @Param("tableName") String tableName, @Param("originalIdList") List<String> originalIdList);

    List<SysTenantResource> selectSysTenantResourceListByTenantKeyAndTableName(@Param("tenantKey") String tenantKey, @Param("tableName") String tableName);

    List<Long> selectMenuIdListByTenantKey(String tenantKey);

    List<Long> selectResourceOriginalIdListByTenantKeyAndTableName(@Param("tenantKey") String tenantKey, @Param("tableName") String tableName);

    List<Long> selectUserIdListByTenantKey(String tenantKey);

    List<Long> selectRoleIdListByTenantKey(String tenantKey);

    List<Long> selectPostIdListByTenantKey(String tenantKey);

    List<Long> selectDeptIdListByTenantKey(String tenantKey);

    List<SysMenu> selectMenuListByTenantKey(String tenantKey);

    /**
     * 新增租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    int insertSysTenantResource(SysTenantResource sysTenantResource);

    /**
     * 修改租户-资源映射关系
     * 
     * @param sysTenantResource 租户-资源映射关系
     * @return 结果
     */
    int updateSysTenantResource(SysTenantResource sysTenantResource);

    /**
     * 删除租户-资源映射关系
     * 
     * @param id 租户-资源映射关系ID
     * @return 结果
     */
    int deleteSysTenantResourceById(Long id);

    /**
     * 批量删除租户-资源映射关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysTenantResourceByIds(List<Long> ids);

    int deleteSysTenantResourceByIdArr(String[] ids);

    void deleteSysTenantResourceMenuByTenantKey(String tenantKey);

    void deleteSysTenantResourceDeptByTenantKey(String tenantKey);

    void deleteSysTenantResourceRoleByTenantKey(String tenantKey);

    void deleteSysTenantResourcePostByTenantKey(String tenantKey);

    void deleteSysTenantResourceUserByTenantKey(String tenantKey);

    void insertSysTenantResourceMenuList(@Param("tenantKey") String tenantKey, @Param("menuList") List<SysMenu> menuList);

    void insertSysTenantResourceDeptList(@Param("tenantKey") String tenantKey, @Param("deptList") List<SysDept> deptList);

    void insertSysTenantResourceRoleList(@Param("tenantKey") String tenantKey, @Param("roleList") List<SysRole> roleList);

    void insertSysTenantResourcePostList(@Param("tenantKey") String tenantKey, @Param("postList") List<SysPost> postList);

    void insertSysTenantResourceUserList(@Param("tenantKey") String tenantKey, @Param("userList") List<SysUser> userList);

}
