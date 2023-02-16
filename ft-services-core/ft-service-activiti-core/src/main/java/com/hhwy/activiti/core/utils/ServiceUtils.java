package com.hhwy.activiti.core.utils;

import com.alibaba.fastjson.JSON;
import com.hhwy.common.core.utils.SpringUtils;
import com.hhwy.common.core.utils.http.HttpUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.security.service.TokenService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ServiceUtils {

    public static TokenService tokenService;

    public static LoadBalancerClient loadBalancerClient;

    /**
     * <br>方法描述：从nacos中获取服务信息
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/26 10:57
     * <br>备注：无
     */
    public static  ServiceInstance getServiceInstance(String serviceName){
        if(loadBalancerClient==null){
            loadBalancerClient = SpringUtils.getBean(LoadBalancerClient.class);
        }
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceName);
        if(serviceInstance==null){
            try {
                Thread.sleep(1000);
            }catch (Exception e){}
            serviceInstance = loadBalancerClient.choose(serviceName);
        }
        Assert.notNull(serviceInstance,serviceName+"服务不可用");
        return serviceInstance;
    }

    public static void send2TaskListenerService(String serviceName, String uri, DelegateTask delegateTask){
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization",tokenResolver());
        headerMap.put("tenantKey",tokenService.getTenantKey());
        String url = urlResolver(serviceName, uri);
        String param = JSON.toJSONString(delegateTask);
        try {
            AjaxResult result = JSON.parseObject(HttpUtils.sendPost(url, param, headerMap),AjaxResult.class);
            if (!AjaxResult.isSuccess(result)) {
                throw new RuntimeException("调用接口: url=" + url +" 异常, 异常信息: msg= "+result.get("msg")+ ", 参数: param=" + param);
            }
        }catch (Exception e){
            throw new RuntimeException("调用接口: url=" + url + " 异常, 异常信息: msg= "+e.getMessage() + ", 参数: param=" + param);
        }
    }

    public static void send2ExecutionListenerService(String serviceName, String uri, DelegateExecution execution){
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization",tokenResolver());
        headerMap.put("tenantKey",tokenService.getTenantKey());
        String url = urlResolver(serviceName, uri);
        String param = JSON.toJSONString(execution);
        try {
            AjaxResult result = JSON.parseObject(HttpUtils.sendPost(url, param, headerMap),AjaxResult.class);
            if (!AjaxResult.isSuccess(result)) {
                throw new RuntimeException("调用接口: url=" + url +" 异常, 异常信息: msg= "+result.get("msg")+ ", 参数: param=" + param);
            }
        }catch (Exception e){
            throw new RuntimeException("调用接口: url=" + url + " 异常, 异常信息: msg= "+e.getMessage() + ", 参数: param=" + param);
        }

    }

    public static String urlResolver(String serviceName, String uri){
        ServiceInstance serviceInstance = getServiceInstance(serviceName);
        String url = serviceInstance.getUri().toString();
        uri = uri.startsWith("/") ? uri : "/"+uri;
        url +=uri;
        return url;
    }

    public static String tokenResolver(){
        if(tokenService == null){
            tokenService = SpringUtils.getBean(TokenService.class);
        }
        String token = tokenService.getLoginUser().getToken();
        return token;
    }
}
