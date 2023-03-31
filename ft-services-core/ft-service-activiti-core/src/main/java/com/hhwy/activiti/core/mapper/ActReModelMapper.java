package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.vo.ActReModelVo;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActReModelMapper {

    /**
     * 查询所有已配置的流程图
     * @return
     */
    List<ActReModelVo> getAllModel();
}
