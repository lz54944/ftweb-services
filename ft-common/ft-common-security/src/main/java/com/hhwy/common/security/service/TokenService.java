package com.hhwy.common.security.service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.utils.token.TokenUtils;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hhwy.common.core.constant.CacheConstants;
import com.hhwy.common.core.utils.IdUtils;
import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.ServletUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.ip.IpUtils;
import com.hhwy.common.redis.service.RedisService;
import com.hhwy.system.api.model.LoginUser;

/**
 * token验证处理
 *
 * @author hhwy
 */
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    @Value("${project.token-expire-time-second:3600}")
    private long EXPIRE_TIME;

    protected static final long MILLIS_SECOND = 1000;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser, String tenantKey) {
        // 生成token
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        loginUser.setTenantKey(tenantKey);
        loginUser.setUserid(loginUser.getSysUser().getUserId());
        loginUser.setUsername(loginUser.getSysUser().getUserName());
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // 保存或更新用户token
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("expires_in", EXPIRE_TIME);
        redisService.setCacheObject(TokenUtils.getTokenKey(token,tenantKey), loginUser, EXPIRE_TIME, TimeUnit.SECONDS);
        return map;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    public String getToken() {
        return getLoginUser(ServletUtils.getRequest()).getToken();
    }

    public SysUser getSysUser() {
        LoginUser loginUser = getLoginUser(ServletUtils.getRequest());
        if(loginUser==null){
            return null;
        }
        return loginUser.getSysUser();
    }

    public SysTenant getTenant() {
        return getSysUser().getTenant();
    }

    public String getTenantKey() {
        return getLoginUser(ServletUtils.getRequest()).getTenantKey();
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        String tenantKey = SecurityUtils.getTenantKey();
        if (StringUtils.isNotEmpty(token)) {
            String userKey = TokenUtils.getTokenKey(token,tenantKey);
            LoginUser user = redisService.getCacheObject(userKey);
            return user;
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    public void delLoginUser(String token,String tenantKey) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = TokenUtils.getTokenKey(token,tenantKey);
            redisService.deleteObject(userKey);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + EXPIRE_TIME * MILLIS_SECOND);
        // 根据uuid将loginUser缓存
        String userKey = TokenUtils.getTokenKey(loginUser.getToken(),loginUser.getTenantKey());
        redisService.setCacheObject(userKey, loginUser, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void storageExpireTime(){
        redisService.setCacheObject(CacheConstants.EXPIRE_TIME_KEY, (int)EXPIRE_TIME, EXPIRE_TIME, TimeUnit.SECONDS);
    }


    public void setRoleAndPermissionUpdateCollection(){
        Collection<String> keys = redisService.keys(TokenUtils.getTokenKey(getTenantKey()));
        if (CollectionUtils.isNotEmpty(keys)) {
            List<String> collect = keys.stream().collect(Collectors.toList());
            redisService.setCacheList(CacheConstants.ROLE_PERMISSION_UPDATE_COLLECTION_KEY,collect);
        }
    }

    public List<String> getTenantKeyListForUser(){
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(getTenantKey());
        if (!getTenantKey().equals(Constants.MASTER_TENANT_KEY) && getTenant().getUserStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }
        return tenantKeyList;
    }
    public List<String> getTenantKeyListForMenu(){
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(getTenantKey());
        if (!getTenantKey().equals(Constants.MASTER_TENANT_KEY) && getTenant().getMenuStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }
        return tenantKeyList;
    }
    public List<String> getTenantKeyListForDept(){
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(getTenantKey());
        if (!getTenantKey().equals(Constants.MASTER_TENANT_KEY) && getTenant().getDeptStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }
        return tenantKeyList;
    }
    public List<String> getTenantKeyListForRole(){
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(getTenantKey());
        if (!getTenantKey().equals(Constants.MASTER_TENANT_KEY) && getTenant().getRoleStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }
        return tenantKeyList;
    }
    public List<String> getTenantKeyListForPost(){
        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(getTenantKey());
        if (!getTenantKey().equals(Constants.MASTER_TENANT_KEY) && getTenant().getPostStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }
        return tenantKeyList;
    }

}