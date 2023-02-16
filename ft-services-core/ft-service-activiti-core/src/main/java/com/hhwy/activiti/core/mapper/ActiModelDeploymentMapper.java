package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.po.ActiModelDeployment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper接口
 *
 * @author jzq
 * @date 2021-07-28
 */
public interface ActiModelDeploymentMapper {

    ActiModelDeployment selectActiModelDeploymentById(String modelId);

    String selectModelIdByDeploymentId(String deploymentId);

    List<ActiModelDeployment> selectActiModelDeploymentList(ActiModelDeployment actiModelDeployment);

    int insertActiModelDeployment(@Param("modelId") String modelId, @Param("deploymentId") String deploymentId);

    int updateActiModelDeployment(ActiModelDeployment actiModelDeployment);

    int deleteActiModelDeploymentByDeploymentId(String deploymentId);

    int deleteActiModelDeploymentById(String modelId);

    int deleteActiModelDeploymentByIds(String[] modelIds);
}
