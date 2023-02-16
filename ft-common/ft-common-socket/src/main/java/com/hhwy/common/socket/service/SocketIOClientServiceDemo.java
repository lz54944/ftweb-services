package com.hhwy.common.socket.service;

import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * 案例
 */
@Slf4j
//@Service
public class SocketIOClientServiceDemo implements ISocketIOClientService {


    @Override
    public void publishEvents(Socket socket) {
        // 发布事件`push_data_event` -> 接收服务端消息
        socket.on("push_data_event_client", objects -> log.debug("服务端触发push_data_event_client事件，并发送消息:" + objects[0].toString()));

        // 发布事件`myBroadcast` -> 接收服务端广播消息
        socket.on("myBroadcast_client", objects -> log.debug("服务端触发myBroadcast_client事件，并发送消息：" + objects[0].toString()));
    }

    @Override
    public void emitEvents(Socket socket) {
        // 触发服务端自定义事件`push_data_event_server` -> 向服务端发送消息
        socket.emit("push_data_event_server", "客户端触发push_data_event_server事件，并发送消息：" + System.currentTimeMillis());
    }
}
