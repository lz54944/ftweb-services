package com.hhwy.common.socket.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Order(value=1)
public class ClientRunner implements CommandLineRunner {

    @Autowired
    private SocketIOClientHandler socketIOClientHandler;

    @Override
    public void run(String... args) {
        socketIOClientHandler.start();
        System.out.println("socket.io客户端启动成功！");
    }

}
