package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.vo.ActReModelVo;
import java.util.List;

public interface IActReModelService {

    /**
     * 查询所有已配置的流程图
     * @return
     */
    List<ActReModelVo> getAllModel();
}
