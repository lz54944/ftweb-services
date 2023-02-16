package com.hhwy.demo.test.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.hhwy.common.socket.core.SocketIOServer;
import com.hhwy.common.socket.service.ISocketIOServerService;
import com.hhwy.common.socket.utils.SocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
//@Service
public class SocketIOServerService implements ISocketIOServerService {

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void addEventListeners() {
        // 声明并监听事件`client_info_event_server`
        socketIOServer.addEventListener("push_data_event_server", String.class, (client, data, ackSender) -> {
            // 客户端推送`client_info_event`事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
            String clientIp = SocketUtils.getIpByClient(client);
            log.debug(clientIp + " ************ 客户端：" + data);
        });

        new Thread(() -> {
            while (true) {
                try {
                    // 每3秒发送一次广播消息
                    Thread.sleep(3000);
                    //触发客户端自定义事件myBroadcast_client
                    broadcastMessage("myBroadcast_client", "广播消息 " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * <br>方法描述：推送信息给指定客户端
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/17 16:27
     * <br>备注：无
     */
    @Override
    public void pushMessageToClient(String eventName, String clientId, Object ... data) {
        SocketIOClient client = socketIOServer.getClientHolder().get(clientId);
        if (client != null) {
            client.sendEvent(eventName, data);
        }
    }

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

}
