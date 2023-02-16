package com.hhwy.demo.mapper;

import com.hhwy.demo.domain.Demo;

/**
 * 参数配置 数据层
 *
 * @author hhwy
 */
public interface DemoMapper {
    int cs (String sql);

    int add(Demo demo);
}