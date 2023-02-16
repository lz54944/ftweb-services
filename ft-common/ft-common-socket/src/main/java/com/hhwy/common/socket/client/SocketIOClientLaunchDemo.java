package com.hhwy.common.socket.client;

import com.hhwy.common.core.utils.UUIDUtils;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

/*IO.socket(url)：与指定的socket.io服务端建立连接
socket.emit：发送数据到服务端事件
socket.on： 监听服务端事件*/
@Slf4j
public class SocketIOClientLaunchDemo {

    public static void main(String[] args) throws Exception{
        // 服务端socket.io连接通信地址
        String url = "http://192.168.1.66:8888";
        IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        //options.reconnectionAttempts = -1; //重连次数
        // 失败重连的时间间隔
        options.reconnectionDelay = 1000;
        // 连接超时时间(ms)
        options.timeout = 3000;
        // clientId: 唯一标识 传给服务端存储
        String clientId = UUIDUtils.getShortUuid();
        final Socket socket = IO.socket(url + "?clientId="+clientId, options);

        //声明内置事件 connect
        socket.on(Socket.EVENT_CONNECT, objects -> {log.debug("SocketIOServer("+url+")连接成功..."); /*socket.send("hello...");*/});

        //声明内置事件 connect_error
        socket.on(Socket.EVENT_CONNECT_ERROR, objects ->log.debug("SocketIOServer("+url+")连接失败..."));

        //声明内置事件 disconnect
        socket.on(Socket.EVENT_DISCONNECT, objects ->  log.debug("SocketIOServer("+url+")连接断开..."));

        //声明内置事件 reconnecting
        socket.on(Socket.EVENT_RECONNECTING, objects ->  log.debug("SocketIOServer("+url+")正在重连..."));

        //声明内置事件 reconnect
        socket.on(Socket.EVENT_RECONNECT, objects ->  log.debug("SocketIOServer("+url+")成功重连..."));

        publishEvents(socket);

        //连接服务端
        socket.connect();

        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            emitEvents(socket);
        }
    }

    public static void publishEvents(Socket socket){
        // 发布事件`push_data_event` -> 接收服务端消息
        socket.on("push_data_event_client", objects -> log.debug("服务端触发push_data_event_client事件，并发送消息:" + objects[0].toString()));

        // 发布事件`myBroadcast` -> 接收服务端广播消息
        socket.on("myBroadcast_client", objects -> log.debug("服务端触发myBroadcast_client事件，并发送消息：" + objects[0].toString()));
    }

    public static void emitEvents(Socket socket){
        // 触发服务端自定义事件`push_data_event_server` -> 向服务端发送消息
        socket.emit("push_data_event_server", "客户端触发push_data_event_server事件，并发送消息：" + System.currentTimeMillis());
    }
}
