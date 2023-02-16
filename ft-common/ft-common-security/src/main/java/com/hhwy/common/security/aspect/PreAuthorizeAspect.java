package com.hhwy.common.security.aspect;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import com.hhwy.common.core.constant.CacheConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.YamlUtil;
import com.hhwy.common.core.utils.token.TokenUtils;
import com.hhwy.common.redis.service.RedisService;
import com.hhwy.system.api.RemoteUserService;
import com.hhwy.system.api.domain.SysUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;
import com.hhwy.common.core.exception.PreAuthorizeException;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.model.LoginUser;

/**
 * 自定义权限实现
 *
 * @author hhwy
 */
@Aspect
@Component
public class PreAuthorizeAspect {

    private final TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    /**
     * 数组为0时
     */
    private static final Integer ARRAY_EMPTY = 0;

    public PreAuthorizeAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(com.hhwy.common.security.annotation.PreAuthorize)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (YamlUtil.isDev()) {
            return point.proceed();
        }
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        if (annotation == null) {
            return point.proceed();
        }

        //更新当前用户权限信息
        updateRolePermission();

        if (StringUtils.isNotEmpty(annotation.hasPermi())) {
            if (hasPermi(annotation.hasPermi())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("没有`"+annotation.hasPermi()+"`的权限，请联系管理员授权");
        } else if (StringUtils.isNotEmpty(annotation.lacksPermi())) {
            if (lacksPermi(annotation.lacksPermi())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("含有`"+annotation.lacksPermi()+"`的权限，请联系管理员删除授权");
        } else if (ARRAY_EMPTY < annotation.hasAnyPermi().length) {
            if (hasAnyPermi(annotation.hasAnyPermi())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("没有`"+annotation.hasAnyPermi()+"`的权限，请联系管理员授权");
        } else if (StringUtils.isNotEmpty(annotation.hasRole())) {
            if (hasRole(annotation.hasRole())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("没有`"+annotation.hasRole()+"`的角色，请联系管理员授权");
        } else if (StringUtils.isNotEmpty(annotation.lacksRole())) {
            if (lacksRole(annotation.lacksRole())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("含有`"+annotation.lacksRole()+"`的角色，请联系管理员删除授权");
        } else if (ARRAY_EMPTY < annotation.hasAnyRoles().length) {
            if (hasAnyRoles(annotation.hasAnyRoles())) {
                return point.proceed();
            }
            throw new PreAuthorizeException("没有`"+annotation.hasAnyRoles()+"`的角色，请联系管理员授权");
        }

        return point.proceed();
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        LoginUser userInfo = tokenService.getLoginUser();
        if (StringUtils.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getPermissions())) {
            return false;
        }
        return hasPermissions(userInfo.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return hasPermi(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String[] permissions) {
        LoginUser userInfo = tokenService.getLoginUser();
        if (StringUtils.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getPermissions())) {
            return false;
        }
        Collection<String> authorities = userInfo.getPermissions();
        for (String permission : permissions) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        LoginUser userInfo = tokenService.getLoginUser();
        if (StringUtils.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getRoles())) {
            return false;
        }
        for (String roleKey : userInfo.getRoles()) {
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String[] roles) {
        LoginUser userInfo = tokenService.getLoginUser();
        if (StringUtils.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getRoles())) {
            return false;
        }
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    //更新当前用户权限信息
    private void updateRolePermission(){
        List<String> rolePermissionUpdateCollection = redisService.getCacheList(CacheConstants.ROLE_PERMISSION_UPDATE_COLLECTION_KEY);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(rolePermissionUpdateCollection)) {
            SysUser sysUser = tokenService.getSysUser();
            String tenantKey = sysUser.getTenant().getTenantKey();
            String token = tokenService.getToken();
            boolean contains = rolePermissionUpdateCollection.contains(TokenUtils.getTokenKey(token,tenantKey));
            if (contains) {
                R<LoginUser> userInfo = remoteUserService.getUserInfo(tenantKey, sysUser.getUserName());
                LoginUser loginUser = userInfo.getData();
                tokenService.setLoginUser(loginUser);
                rolePermissionUpdateCollection.remove(TokenUtils.getTokenKey(token,tenantKey));
                redisService.deleteObject(CacheConstants.ROLE_PERMISSION_UPDATE_COLLECTION_KEY);
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(rolePermissionUpdateCollection))  {
                    redisService.setCacheList(CacheConstants.ROLE_PERMISSION_UPDATE_COLLECTION_KEY,rolePermissionUpdateCollection);
                }
            }
        }
    }
}
