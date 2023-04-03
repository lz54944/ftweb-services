package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.vo.ActReModelVo;
import com.hhwy.activiti.core.mapper.ActReModelMapper;
import com.hhwy.activiti.core.service.IActReModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActReModelService implements IActReModelService {

    @Autowired
    private ActReModelMapper actReModelMapper;

    /**
     * 查询所有已配置的流程图
     * @return
     */
    @Override
    public List<ActReModelVo> getAllModel() {
        return actReModelMapper.getAllModel();
    }
}
