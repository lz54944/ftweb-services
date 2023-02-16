package com.hhwy.activiti.api;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.activiti.api.factory.RemoteActivitiFallbackFactory;
import com.hhwy.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import com.hhwy.common.core.constant.ServiceNameConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 流程服务
 *
 * @author jzq
 */
@FeignClient(contextId = "remoteActivitiService", value = ServiceNameConstants.ACTIVITI_SERVICE, fallbackFactory = RemoteActivitiFallbackFactory.class)
public interface RemoteActivitiService {

     /**
        * <br>方法描述：获取业务数据与流程关联关系的详细信息
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/9/1 14:23
        * <br>备注：无
        */
    @GetMapping(value = "/act/business/{tenantKey}/{tableName}/{businessId}")
    R<ActiBusinessInfo> getInfo(@PathVariable("tenantKey") String tenantKey, @PathVariable("tableName") String tableName, @PathVariable("businessId") String businessId);
}
