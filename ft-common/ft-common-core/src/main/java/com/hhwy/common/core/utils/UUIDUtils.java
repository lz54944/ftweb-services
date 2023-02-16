package com.hhwy.common.core.utils;

import java.util.UUID;

/**
 * 生成uuid工具类
 *
 * @author jzq
 * @date 20190918
 */
public class UUIDUtils {

    public static String getID() {
        return getShortUuid();
    }

    /**
     * <br>方法描述：生成64位随机数
     * <br>接收参数:
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2020/3/19 17:35
     * <br>备注：无
     */
    public static String getShortUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUuid(int number) {
        return UUID.randomUUID().toString().substring(0, number);
    }
}
