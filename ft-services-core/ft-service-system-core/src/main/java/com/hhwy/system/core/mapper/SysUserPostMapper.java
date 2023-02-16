package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysUserPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与岗位关联表 数据层
 *
 * @author hhwy
 */
public interface SysUserPostMapper {
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserPostByUserId(@Param("userId") Long userId, @Param("tenantKey") String tenantKey);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countUserPostById(@Param("postId") Long postId, @Param("tenantKey") String tenantKey);

    /**
     * 批量删除用户和岗位关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserPost(@Param("ids") Long[] ids, @Param("tenantKey") String tenantKey);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    int batchUserPost(List<SysUserPost> userPostList);
}
