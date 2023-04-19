package com.hhwy.system.api.factory;

import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.api.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.hhwy.common.core.domain.R;
import com.hhwy.system.api.RemoteUserService;
import com.hhwy.system.api.model.LoginUser;
import java.util.List;

/**
 * 用户服务降级处理
 *
 * @author hhwy
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public R<LoginUser> getUserInfo(String tenantKey, String username) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByRoleIds(String roleIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByOriginalRoleIds(String originalRoleIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByRoleIdsAndDeptIds(String roleIds, String deptIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByOriginalRoleIdsAndDeptIds(String originalRoleIds, String deptIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> getUserInfoByUsername(String username) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByUsernames(String usernames) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserInfoByUserId(Long userId) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByUserIds(String userIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByOriginalUserIds(String originalUserIds) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByNickName(String nickName) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult selectUserAllList(Long deptId) {
                throw new RuntimeException("获取用户失败");
            }
        };
    }
}
