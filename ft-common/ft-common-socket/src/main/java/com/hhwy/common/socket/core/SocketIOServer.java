package com.hhwy.common.socket.core;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketIOServer extends com.corundumstudio.socketio.SocketIOServer {

    /**
     * 存放已连接的客户端
     */
    private Map<String, SocketIOClient> clientHolder = new ConcurrentHashMap<>();

    public SocketIOServer(Configuration configuration) {
        super(configuration);
    }

    public Map<String, SocketIOClient> getClientHolder() {
        return clientHolder;
    }

    public void setClientHolder(Map<String, SocketIOClient> clientHolder) {
        this.clientHolder = clientHolder;
    }
}
