package com.hhwy.common.socket.config;

import com.corundumstudio.socketio.SocketConfig;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.socket.controller.SocketIOController;
import com.hhwy.common.socket.core.SocketIOServer;
import com.hhwy.common.socket.core.ServerRunner;
import com.hhwy.common.socket.core.SocketIOServerHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.net.InetAddress;

public class SocketIOServerConfig {

    @Value("${socketio.host:}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    //socket连接数大小（如只监听一个端口boss线程组为1即可）
    @Value("${socketio.bossThreadCount:1}")
    private int bossThreadCount;

    @Value("${socketio.workThreadCount:100}")
    private int workThreadCount;

    @Value("${socketio.allowCustomRequests:true}")
    private boolean allowCustomRequests;

    //协议升级超时时间（毫秒），默认10秒。HTTP握手升级为ws协议超时时间
    @Value("${socketio.upgradeTimeout:1000000}")
    private int upgradeTimeout;

    //Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
    @Value("${socketio.pingTimeout:6000000}")
    private int pingTimeout;

    //Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
    @Value("${socketio.pingInterval:25000}")
    private int pingInterval;

    @Bean
    public SocketIOServer socketIOServer() throws Exception{
        if(StringUtils.isEmpty(host)){
            host = InetAddress.getLocalHost().getHostAddress();
        }
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossThreadCount);
        config.setWorkerThreads(workThreadCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        return new SocketIOServer(config);
    }

    @Bean
    public ServerRunner serverRunner() {
        return new ServerRunner();
    }

    @Bean
    public SocketIOServerHandler socketIOServerHandler() {
        return new SocketIOServerHandler();
    }

    @Bean
    public SocketIOController socketIOController() {
        return new SocketIOController();
    }

}
