package com.hhwy.common.socket.config;

import com.hhwy.common.socket.core.ClientRunner;
import com.hhwy.common.socket.core.SocketIOClientHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class SocketIOClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public ClientRunner clientRunner() {
        return new ClientRunner();
    }

    @Bean
    @ConditionalOnMissingBean
    public SocketIOClientHandler socketIOClientHandler() {
        return new SocketIOClientHandler();
    }

}
