package com.hhwy.common.socket.core;

import com.hhwy.common.socket.service.ISocketIOServerService;
import com.hhwy.common.socket.utils.SocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import javax.annotation.PreDestroy;

@Slf4j
public class SocketIOServerHandler implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    private SocketIOServer socketIOServer;

    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String clientId = SocketUtils.getParamsByClient(client);
            log.debug("************************ 客户端-"+clientId+": " + SocketUtils.getIpByClient(client) + " 已成功连接 ************************");
            if (clientId != null) {
                socketIOServer.getClientHolder().put(clientId, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String clientIp = SocketUtils.getIpByClient(client);
            String clientId = SocketUtils.getParamsByClient(client);
            log.debug(clientIp + "************************ 客户端-"+clientId+": " + clientIp + " 已断开连接 ************************");
            if (clientId != null) {
                socketIOServer.getClientHolder().remove(clientId);
                client.disconnect();
            }
        });
        //绑定自定义服务
        String[] beanNames = applicationContext.getBeanNamesForType(ISocketIOServerService.class);
        for (String beanName : beanNames) {
            ISocketIOServerService socketIOServerService = (ISocketIOServerService) applicationContext.getBean(beanName);
            socketIOServerService.addEventListeners();
        }
        // 启动服务
        socketIOServer.start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
