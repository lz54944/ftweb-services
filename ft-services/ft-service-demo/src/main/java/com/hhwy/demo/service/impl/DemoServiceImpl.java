package com.hhwy.demo.service.impl;

import com.hhwy.demo.domain.Demo;
import com.hhwy.demo.mapper.DemoMapper;
import com.hhwy.demo.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 参数配置 服务层实现
 *
 * @author hhwy
 */
@Service
public class DemoServiceImpl implements IDemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    @Transactional
    public Boolean test(){
        return true;
    }

    @Override
    public int add(Demo demo) {
        return demoMapper.add(demo);
    }


}
