package com.hhwy.common.socket.service;

public interface ISocketIOServerService {

    void addEventListeners();

    /**
     * 推送信息给指定客户端
     */
    void pushMessageToClient(String eventName, String clientId, Object ... data);

    /**
     * 广播信息
     */
    void broadcastMessage(String eventName, Object ... data);
}
