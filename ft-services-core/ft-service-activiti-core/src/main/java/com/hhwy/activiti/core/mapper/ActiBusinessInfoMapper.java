package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 流程-业务Mapper接口
 * 
 * @author jzq
 * @date 2021-08-24
 */
public interface ActiBusinessInfoMapper {
    /**
     * 查询流程-业务
     * 
     * @param id 流程-业务ID
     * @return 流程-业务
     */
    ActiBusinessInfo selectActiBusinessInfoById(Long id);


    ActiBusinessInfo selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(@Param("businessTableName") String businessTableName, @Param("businessId") String businessId, @Param("tenantKey") String tenantKey);
    /**
     * 查询流程-业务列表
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 流程-业务集合
     */
    List<ActiBusinessInfo> selectActiBusinessInfoList(ActiBusinessInfo actiBusinessInfo);

    /**
     * 新增流程-业务
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 结果
     */
    int insertActiBusinessInfo(ActiBusinessInfo actiBusinessInfo);

    /**
     * 修改流程-业务
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 结果
     */
    int updateActiBusinessInfo(ActiBusinessInfo actiBusinessInfo);

    /**
     * 删除流程-业务
     * 
     * @param id 流程-业务ID
     * @return 结果
     */
    int deleteActiBusinessInfoById(Long id);

    /**
     * 批量删除流程-业务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiBusinessInfoByIds(String[] ids);

}
