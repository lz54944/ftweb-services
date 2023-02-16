package com.hhwy.demo.test.service;

import com.hhwy.demo.domain.Demo;
import com.hhwy.demo.mapper.DemoMapper;
import com.hhwy.demo.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 参数配置 服务层实现
 *
 * @author hhwy
 */
//@Service
public class TestServiceImpl implements IDemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public Boolean test() {
        return null;
    }

    @Override
    public int add(Demo demo) {
        return 0;
    }
}
