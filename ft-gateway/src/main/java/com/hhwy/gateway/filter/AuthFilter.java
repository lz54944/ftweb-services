package com.hhwy.gateway.filter;

import com.hhwy.auth.api.RemoteTokenService;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.YamlUtil;
import com.hhwy.common.core.utils.token.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhwy.common.core.constant.CacheConstants;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.ServletUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.redis.service.RedisService;
import com.hhwy.gateway.config.properties.IgnoreWhiteProperties;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 网关鉴权
 *
 * @author hhwy
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    private RemoteTokenService remoteTokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        if (YamlUtil.isDev()) {
            return chain.filter(exchange);
        }
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }

        /*单点认证-开始*/
        Object access_token = null;
        String token = null;
        String tenantKey = null;
        String ssoFlag = request.getQueryParams().getFirst("ssoFlag");
        if ("true".equals(ssoFlag)){ //单点认证请求
            FutureTask<R<Map<String, Object>>> ft = new FutureTask<>(()->{
                if (remoteTokenService==null) {
                    remoteTokenService = SpringUtils.getBean(RemoteTokenService.class);
                }
                Map<String,String> params = new HashMap<>();
                params.put("token",request.getQueryParams().getFirst("token"));
                params.put("key",request.getQueryParams().getFirst("key"));
                params.put("tenantKey",request.getQueryParams().getFirst("tenantKey"));
                R<Map<String, Object>> result = remoteTokenService.ssoValidate(params);
                return result;
            });
            new Thread(ft).start();
            try {
                Map<String, Object> resultMap = ft.get().getData();
                access_token = resultMap.get("access_token");
                if (access_token==null) {
                    return setUnauthorizedResponse(exchange, "单点认证失败", HttpStatus.FORBIDDEN);
                }else {
                    token=access_token+"";
                    tenantKey = request.getQueryParams().getFirst("tenantKey");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*单点认证-结束*/
        }else {
            tenantKey = getTenantKey(request);
            if (StringUtils.isBlank(tenantKey)) {
                return setUnauthorizedResponse(exchange, "租户标识不能为空", HttpStatus.FORBIDDEN);
            }
            token = getToken(request);
            if (StringUtils.isBlank(token)) {
                return setUnauthorizedResponse(exchange, "令牌不能为空", HttpStatus.FORBIDDEN);
            }
        }

        Object userObject = redisService.getCacheObject(TokenUtils.getTokenKey(token, tenantKey));

        if (userObject == null) {
            return setUnauthorizedResponse(exchange, "登录状态已过期", HttpStatus.FOUND);
        }
        JSONObject user = (JSONObject)userObject;
        String userId = user.getString("userid");
        String username = user.getString("username");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(username)) {
            return setUnauthorizedResponse(exchange, "令牌验证失败", HttpStatus.UNAUTHORIZED);
        }
        // 设置过期时间
        redisService.expire(TokenUtils.getTokenKey(token, tenantKey), getExpireTime());
        redisService.expire(CacheConstants.EXPIRE_TIME_KEY, getExpireTime());
        // 设置用户信息到请求
        ServerHttpRequest mutableReq;

        if ("true".equals(ssoFlag)){//单点认证请求
            mutableReq = request.mutate().header(CacheConstants.DETAILS_USER_ID, userId)
                    .header(CacheConstants.DETAILS_USERNAME, ServletUtils.urlEncode(username))
                    .header(CacheConstants.HEADER,access_token+"")
                    .header(CacheConstants.DETAILS_TENANT_KEY,tenantKey).
                    build();
        }else {
            mutableReq = request.mutate().header(CacheConstants.DETAILS_USER_ID, userId)
                    .header(CacheConstants.DETAILS_USERNAME, ServletUtils.urlEncode(username)).build();
        }

        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

        return chain.filter(mutableExchange);
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(httpStatus);

        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(R.fail(response.getStatusCode().value(), msg)));
        }));
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CacheConstants.HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstants.TOKEN_PREFIX)) {
            token = token.replace(CacheConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTenantKey(ServerHttpRequest request) {
        String tenantKey = request.getHeaders().getFirst(CacheConstants.DETAILS_TENANT_KEY);
        return tenantKey;
    }

    @Override
    public int getOrder() {
        return -200;
    }

    private long getExpireTime(){
        Object expireTimeObject = redisService.getCacheObject(CacheConstants.EXPIRE_TIME_KEY);
        if (expireTimeObject == null) {
            return 3600L;
        }else {
            return Long.parseLong(expireTimeObject.toString());
        }
    }

}