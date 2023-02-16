package com.hhwy.common.socket.core;

import com.hhwy.common.socket.service.ISocketIOClientService;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class SocketIOClientHandler implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Value("${socketio.server.uri}")
    private String uri;

    @Value("${socketio.client.id}")
    private String clientId;

    public void start() {
        try {
            // 服务端socket.io连接通信地址
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            //options.reconnectionAttempts = -1; //重连次数
            // 失败重连的时间间隔
            options.reconnectionDelay = 1000;
            // 连接超时时间(ms)
            options.timeout = 3000;
            // clientId: 唯一标识 传给服务端存储
            final Socket socket = IO.socket(uri + "?clientId="+clientId, options);

            //声明内置事件 connect
            socket.on(Socket.EVENT_CONNECT, objects -> {log.debug("************************ SocketIOServer("+uri+")连接成功..."); /*socket.send("hello...");*/});

            //声明内置事件 connect_error
            socket.on(Socket.EVENT_CONNECT_ERROR, objects ->log.debug("************************ SocketIOServer("+uri+")连接失败..."));

            //声明内置事件 disconnect
            socket.on(Socket.EVENT_DISCONNECT, objects ->  log.debug("************************ SocketIOServer("+uri+")连接断开..."));

            //声明内置事件 reconnecting
            socket.on(Socket.EVENT_RECONNECTING, objects ->  log.debug("************************ SocketIOServer("+uri+")正在重连..."));

            //声明内置事件 reconnect
            socket.on(Socket.EVENT_RECONNECT, objects ->  log.debug("************************ SocketIOServer("+uri+")成功重连..."));

            String[] beanNames = applicationContext.getBeanNamesForType(ISocketIOClientService.class);
            for (String beanName : beanNames) {
                ISocketIOClientService socketIOClientService = (ISocketIOClientService) applicationContext.getBean(beanName);
                socketIOClientService.publishEvents(socket);
            }

            //连接服务端
            socket.connect();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
