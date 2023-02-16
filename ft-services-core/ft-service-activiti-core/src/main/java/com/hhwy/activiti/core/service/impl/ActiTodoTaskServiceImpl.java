package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.po.ActiTaskAgent;
import com.hhwy.activiti.core.domain.po.ActiTodoTask;
import com.hhwy.activiti.core.mapper.ActiTaskAgentMapper;
import com.hhwy.activiti.core.mapper.ActiTodoTaskMapper;
import com.hhwy.activiti.core.service.IActiTodoTaskService;
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

import java.util.*;

/**
 * <br>描 述： ActiTodoTaskServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActiTodoTaskServiceImpl implements IActiTodoTaskService {
    @Autowired
    private ActiTodoTaskMapper actiTodoTaskMapper;

    @Autowired
    private ActiTaskAgentMapper actiTaskAgentMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteConfigService remoteConfigService;

    /**
     * 查询待办任务
     *
     * @param id 待办任务ID
     * @return 待办任务
     */
    @Override
    public ActiTodoTask selectActiTodoTaskById(Long id) {
        return actiTodoTaskMapper.selectActiTodoTaskById(id);
    }

    /**
     * 查询待办任务列表
     *
     * @param actiTodoTask 待办任务
     * @return 待办任务
     */
    @Override
    public List<ActiTodoTask> selectActiTodoTaskList(ActiTodoTask actiTodoTask) {
        String tenantKey = tokenService.getTenantKey();
        actiTodoTask.setTenantKey(tenantKey);
        Map<String,String> configMap = new HashMap<>();
        String senderNickName = actiTodoTask.getSenderNickName();
        if (StringUtils.isNotEmpty(senderNickName)) {
            List<SysUser> sysUserList = userService.getUserListByNickName(senderNickName);
            if (CollectionUtils.isNotEmpty(sysUserList)) {
                Map<String, Object> params = new HashMap<>();
                params.put("senderList",sysUserList);
                actiTodoTask.setParams(params);
            }else {
                return null;
            }
        }

        String username = tokenService.getLoginUser().getUsername();
        if (StringUtils.isEmpty(actiTodoTask.getReceiver())) {
            actiTodoTask.setReceiver(username);
        }

        List<ActiTaskAgent> actiTaskAgentList = actiTaskAgentMapper.selectActiTaskAgentByCreateUserAndEndTime(tenantKey,username,new Date());
        if (CollectionUtils.isNotEmpty(actiTaskAgentList)) {
            Map<String, Object> params = new HashMap<>();
            params.put("taskAgentList",actiTaskAgentList);
            actiTodoTask.setParams(params);
        }
        List<ActiTodoTask> actiTodoTasks = actiTodoTaskMapper.selectActiTodoTaskList(actiTodoTask);
        for (ActiTodoTask todoTask : actiTodoTasks) {
            String sender = todoTask.getSender();
            todoTask.setSenderNickName(userService.getNickName(tenantKey, sender));
            String receiver = todoTask.getReceiver();
            todoTask.setReceiverNickName(userService.getNickName(tenantKey, receiver));
            String formUrl = todoTask.getFormUrl();
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
                todoTask.setFormUrl(formUrl);
            }
        }
        return actiTodoTasks;
    }

    /**
     * 新增待办任务
     *
     * @param actiTodoTask 待办任务
     * @return 结果
     */
    @Override
    public int insertActiTodoTask(ActiTodoTask actiTodoTask) {
        actiTodoTask.setCreateTime(DateUtils.getNowDate());
        actiTodoTask.setTenantKey(tokenService.getTenantKey());
        return actiTodoTaskMapper.insertActiTodoTask(actiTodoTask);
    }

    /**
     * 修改待办任务
     *
     * @param actiTodoTask 待办任务
     * @return 结果
     */
    @Override
    public int updateActiTodoTask(ActiTodoTask actiTodoTask) {
        actiTodoTask.setUpdateTime(DateUtils.getNowDate());
        return actiTodoTaskMapper.updateActiTodoTask(actiTodoTask);
    }

    /**
     * 删除待办任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActiTodoTaskByIds(String ids) {
        return actiTodoTaskMapper.deleteActiTodoTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除待办任务信息
     *
     * @param id 待办任务ID
     * @return 结果
     */
    @Override
    public int deleteActiTodoTaskById(Long id) {
        return actiTodoTaskMapper.deleteActiTodoTaskById(id);
    }

    @Override
    public int deleteActiTodoTaskByTaskId(String taskId) {
        return actiTodoTaskMapper.deleteActiTodoTaskByTaskId(taskId);
    }

    @Override
    public ActiTodoTask selectActiTodoTaskByTaskId(String taskId) {
        return actiTodoTaskMapper.selectActiTodoTaskByTaskId(taskId);
    }

    @Override
    public int updateReceiverByTaskId(String taskId, String assignee) {
        return actiTodoTaskMapper.updateReceiverByTaskId(taskId, assignee);
    }
}
