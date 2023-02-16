package com.hhwy.common.tenant.service;

import com.hhwy.common.socket.core.SocketIOServer;
import com.hhwy.common.socket.service.SocketIOServerServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 案例
 */
@Slf4j
public class TenantSocketIOServerService extends SocketIOServerServiceAdapter {

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * <br>方法描述：广播信息
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/17 16:27
     * <br>备注：默认是向所有的socket连接进行广播，但是不包括发送者自身，如果自己也打算接收消息的话，需要给自己单独发送
     */
    @Override
    public void broadcastMessage(String eventName, Object ... data) {
        socketIOServer.getBroadcastOperations().sendEvent(eventName, data);
    }

    public void broadcastCreatedMessage(Object ... data) {
        broadcastMessage("tenant_created_event", data);
    }

    //租户修改不需要做任何处理
    public void broadcastEditedMessage(Object ... data) {
        broadcastMessage("tenant_edited_event", data);
    }

    public void broadcastDeletedMessage(Object ... data) {
        broadcastMessage("tenant_deleted_event", data);
    }

}
