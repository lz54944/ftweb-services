package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.vo.ActReDeploymentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>描 述： ActReDeploymentMapper
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface ActReDeploymentMapper {

    List<ActReDeploymentVO> selectActReDeploymentByIds(@Param("ids") List<String> ids);

}
