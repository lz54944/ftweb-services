package com.hhwy.common.core.utils;

import com.hhwy.common.core.web.domain.BaseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseEntityUtils {

    /**
     * 补充新增参数
     */
    public static void suppleCreateParams(BaseEntity baseEntity) {
        baseEntity.setCreateUserId(SecurityUtils.getUserId());
        baseEntity.setCreateUser(SecurityUtils.getUserName());
        baseEntity.setCreateTime(new Date());
        baseEntity.setUpdateUserId(SecurityUtils.getUserId());
        baseEntity.setUpdateUser(SecurityUtils.getUserName());
        baseEntity.setUpdateTime(new Date());
        baseEntity.setTenantKey(SecurityUtils.getTenantKey());
    }

    /**
     * 补充更新参数
     */
    public static void suppleUpdateParams(BaseEntity baseEntity) {
        baseEntity.setUpdateUserId(SecurityUtils.getUserId());
        baseEntity.setUpdateUser(SecurityUtils.getUserName());
        baseEntity.setUpdateTime(new Date());
    }

}
