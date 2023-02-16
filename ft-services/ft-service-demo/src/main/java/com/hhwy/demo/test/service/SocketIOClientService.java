package com.hhwy.demo.test.service;

import com.hhwy.common.socket.service.ISocketIOClientService;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 案例
 */
@Slf4j
//@Service
public class SocketIOClientService implements ISocketIOClientService {


    @Override
    public void publishEvents(Socket socket) {
        // 发布事件  -> 接收服务端消息
        socket.on("tenant_created_event", objects -> log.debug("租户被添加，并发送消息:" + objects[0].toString()));

        socket.on("tenant_edited_event",  objects -> log.debug("租户被修改，并发送消息：" + objects[0].toString()));

        socket.on("tenant_deleted_event", objects -> log.debug("租户被删除，并发送消息：" + objects[0].toString()));
    }

    @Override
    public void emitEvents(Socket socket) {
        // 触发服务端自定义事件`push_data_event_server` -> 向服务端发送消息
        socket.emit("push_data_event_server", "客户端触发push_data_event_server事件，并发送消息：" + System.currentTimeMillis());
    }
}
