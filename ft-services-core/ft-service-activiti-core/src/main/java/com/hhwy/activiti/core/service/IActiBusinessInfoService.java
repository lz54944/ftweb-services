package com.hhwy.activiti.core.service;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import java.util.List;

/**
 * 流程-业务Service接口
 * 
 * @author jzq
 * @date 2021-08-24
 */
public interface IActiBusinessInfoService {
    /**
     * 查询流程-业务
     * 
     * @param id 流程-业务ID
     * @return 流程-业务
     */
    ActiBusinessInfo selectActiBusinessInfoById(Long id);

    ActiBusinessInfo selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(String tableName, String businessId, String tenantKey);

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
     * 批量删除流程-业务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteActiBusinessInfoByIds(String ids);

    /**
     * 删除流程-业务信息
     * 
     * @param id 流程-业务ID
     * @return 结果
     */
    int deleteActiBusinessInfoById(Long id);
}
