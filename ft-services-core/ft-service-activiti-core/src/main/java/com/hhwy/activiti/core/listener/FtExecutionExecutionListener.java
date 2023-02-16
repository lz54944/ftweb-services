package com.hhwy.activiti.core.listener;

import com.hhwy.activiti.core.utils.ServiceUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;

/**
 * <br>描 述： 执行监听器
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/5 18:02
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class FtExecutionExecutionListener implements ExecutionListener {

    public FixedValue service;

    public FixedValue uri;

    @Override
    public void notify(DelegateExecution execution) {
        ServiceUtils.send2ExecutionListenerService(service.getExpressionText(),uri.getExpressionText(),execution);
    }
}
