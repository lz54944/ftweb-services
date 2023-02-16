package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.activiti.core.mapper.ActiBusinessInfoMapper;
import com.hhwy.activiti.core.service.IActiBusinessInfoService;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.RemoteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程-业务Service业务层处理
 * 
 * @author jzq
 * @date 2021-08-24
 */
@Service
public class ActiBusinessInfoServiceImpl implements IActiBusinessInfoService {
    @Autowired
    private ActiBusinessInfoMapper actiBusinessInfoMapper;

    @Autowired
    private RemoteConfigService remoteConfigService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询流程-业务
     * 
     * @param id 流程-业务ID
     * @return 流程-业务
     */
    @Override
    public ActiBusinessInfo selectActiBusinessInfoById(Long id) {
        return actiBusinessInfoMapper.selectActiBusinessInfoById(id);
    }

    @Override
    public ActiBusinessInfo selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(String tableName, String businessId, String tenantKey) {
        ActiBusinessInfo actiBusinessInfo = actiBusinessInfoMapper.selectActiBusinessInfoByTableNameAndBusinessIdAndTenantKey(tableName, businessId, tenantKey);
        if (actiBusinessInfo != null) {
            String formUrl = actiBusinessInfo.getFormUrl();
            if (StringUtils.isNotEmpty(formUrl) && StringUtils.startsWithAny(formUrl, "{")) {
                int index = formUrl.indexOf("}");
                String key = formUrl.substring(1,index);
                String uri = formUrl.substring(index+1);
                String host = remoteConfigService.getConfigKey(key).getData();
                Assert.notNull(host,"请在参数设置中配置变量`"+key+"`的值");
                formUrl = host+uri;
                actiBusinessInfo.setFormUrl(formUrl);
            }
        }
        return actiBusinessInfo;
    }

    /**
     * 查询流程-业务列表
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 流程-业务
     */
    @Override
    public List<ActiBusinessInfo> selectActiBusinessInfoList(ActiBusinessInfo actiBusinessInfo) {
        actiBusinessInfo.setTenantKey(tokenService.getTenantKey());
        Map<String,String> configMap = new HashMap<>();
        List<ActiBusinessInfo> actiBusinessInfos = actiBusinessInfoMapper.selectActiBusinessInfoList(actiBusinessInfo);
        for (ActiBusinessInfo businessInfo : actiBusinessInfos) {
            String formUrl = businessInfo.getFormUrl();
            if (StringUtils.isNotEmpty(formUrl) && StringUtils.startsWithAny(formUrl, "{")) {
                int index = formUrl.indexOf("}");
                String key = formUrl.substring(1,index);
                String uri = formUrl.substring(index+1);
                String host = configMap.get(key);
                if (StringUtils.isEmpty(host)) {
                    host = remoteConfigService.getConfigKey(key).getData();
                    Assert.notNull(host,"请在参数设置中配置变量`"+key+"`的值");
                    configMap.put(key,host);
                }
                formUrl = host+uri;
                businessInfo.setFormUrl(formUrl);
            }
        }
        return actiBusinessInfos;
    }

    /**
     * 新增流程-业务
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 结果
     */
    @Override
    public int insertActiBusinessInfo(ActiBusinessInfo actiBusinessInfo) {
        actiBusinessInfo.setCreateTime(DateUtils.getNowDate());
        return actiBusinessInfoMapper.insertActiBusinessInfo(actiBusinessInfo);
    }

    /**
     * 修改流程-业务
     * 
     * @param actiBusinessInfo 流程-业务
     * @return 结果
     */
    @Override
    public int updateActiBusinessInfo(ActiBusinessInfo actiBusinessInfo) {
        actiBusinessInfo.setUpdateTime(DateUtils.getNowDate());
        return actiBusinessInfoMapper.updateActiBusinessInfo(actiBusinessInfo);
    }

    /**
     * 删除流程-业务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActiBusinessInfoByIds(String ids) {
        return actiBusinessInfoMapper.deleteActiBusinessInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程-业务信息
     * 
     * @param id 流程-业务ID
     * @return 结果
     */
    @Override
    public int deleteActiBusinessInfoById(Long id) {
        return actiBusinessInfoMapper.deleteActiBusinessInfoById(id);
    }
}
