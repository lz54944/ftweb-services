package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;
import com.hhwy.activiti.core.mapper.ActiTodoTaskHistoryMapper;
import com.hhwy.activiti.core.service.IActiTodoTaskHistoryService;
import com.hhwy.activiti.core.utils.UserService;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.RemoteConfigService;
import com.hhwy.system.api.domain.SysUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已办任务Service业务层处理
 * 
 * @author jzq
 * @date 2021-08-03
 */
@Service
public class ActiTodoTaskHistoryServiceImpl implements IActiTodoTaskHistoryService {

    @Autowired
    private ActiTodoTaskHistoryMapper actiTodoTaskHistoryMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteConfigService remoteConfigService;

    /**
     * 查询已办任务
     * 
     * @param id 已办任务ID
     * @return 已办任务
     */
    @Override
    public ActiTodoTaskHistory selectActiTodoTaskHistoryById(Long id) {
        return actiTodoTaskHistoryMapper.selectActiTodoTaskHistoryById(id);
    }

    @Override
    public ActiTodoTaskHistory selectActiTodoTaskHistoryByNextTaskId(String nextTaskId) {
        return actiTodoTaskHistoryMapper.selectActiTodoTaskHistoryByNextTaskId(nextTaskId);
    }

    @Override
    public ActiTodoTaskHistory selectActiTodoTaskHistoryByPreTaskId(String preTaskId) {
        return actiTodoTaskHistoryMapper.selectActiTodoTaskHistoryByPreTaskId(preTaskId);
    }

    @Override
    public ActiTodoTaskHistory selectActiTodoTaskHistoryByTaskId(String taskId) {
        return actiTodoTaskHistoryMapper.selectActiTodoTaskHistoryByTaskId(taskId);
    }

    /**
     * 查询已办任务列表
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 已办任务
     */
    @Override
    public List<ActiTodoTaskHistory> selectActiTodoTaskHistoryList(ActiTodoTaskHistory actiTodoTaskHistory) {
        actiTodoTaskHistory.setTenantKey(tokenService.getTenantKey());
        Map<String,String> configMap = new HashMap<>();
        String senderNickName = actiTodoTaskHistory.getSenderNickName();
        if (StringUtils.isNotEmpty(senderNickName)) {
            List<SysUser> sysUserList = userService.getUserListByNickName(senderNickName);
            if (CollectionUtils.isNotEmpty(sysUserList)) {
                Map<String, Object> params = new HashMap<>();
                params.put("senderList",sysUserList);
                actiTodoTaskHistory.setParams(params);
            }else {
                return null;
            }
        }

        if (StringUtils.isEmpty(actiTodoTaskHistory.getReceiver())) {
            actiTodoTaskHistory.setReceiver(tokenService.getLoginUser().getUsername());
        }
        List<ActiTodoTaskHistory> actiTodoTaskHistories = actiTodoTaskHistoryMapper.selectActiTodoTaskHistoryList(actiTodoTaskHistory);
        for (ActiTodoTaskHistory todoTaskHistory : actiTodoTaskHistories) {
            String tenantKey = todoTaskHistory.getTenantKey();
            String sender = todoTaskHistory.getSender();
            todoTaskHistory.setSenderNickName(userService.getNickName(tenantKey, sender));
            String receiver = todoTaskHistory.getReceiver();
            todoTaskHistory.setReceiverNickName(userService.getNickName(tenantKey, receiver));
            String agentUser = todoTaskHistory.getAgentUser();
            if (StringUtils.isNotEmpty(agentUser)) {
                todoTaskHistory.setAgentNickName(userService.getNickName(tenantKey, agentUser));
            }else {
                todoTaskHistory.setAgentNickName("-");
            }

            String formUrl = todoTaskHistory.getFormUrl();
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
                todoTaskHistory.setFormUrl(formUrl);
            }
        }

        return actiTodoTaskHistories;
    }

    /**
     * 新增已办任务
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 结果
     */
    @Override
    public int insertActiTodoTaskHistory(ActiTodoTaskHistory actiTodoTaskHistory) {
        actiTodoTaskHistory.setCreateTime(DateUtils.getNowDate());
        actiTodoTaskHistory.setTenantKey(tokenService.getTenantKey());
        return actiTodoTaskHistoryMapper.insertActiTodoTaskHistory(actiTodoTaskHistory);
    }

    /**
     * 修改已办任务
     * 
     * @param actiTodoTaskHistory 已办任务
     * @return 结果
     */
    @Override
    public int updateActiTodoTaskHistory(ActiTodoTaskHistory actiTodoTaskHistory) {
        actiTodoTaskHistory.setUpdateTime(DateUtils.getNowDate());
        return actiTodoTaskHistoryMapper.updateActiTodoTaskHistory(actiTodoTaskHistory);
    }

    /**
     * 删除已办任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActiTodoTaskHistoryByIds(String ids) {
        return actiTodoTaskHistoryMapper.deleteActiTodoTaskHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除已办任务信息
     * 
     * @param id 已办任务ID
     * @return 结果
     */
    @Override
    public int deleteActiTodoTaskHistoryById(Long id) {
        return actiTodoTaskHistoryMapper.deleteActiTodoTaskHistoryById(id);
    }

    @Override
    public int setRecallTask(String taskId) {
        return actiTodoTaskHistoryMapper.setRecallTask(taskId);
    }

    @Override
    public int setBackTask(String taskId) {
        return actiTodoTaskHistoryMapper.setBackTask(taskId);
    }
}
