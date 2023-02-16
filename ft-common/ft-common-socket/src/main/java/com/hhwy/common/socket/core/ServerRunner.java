package com.hhwy.common.socket.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Order(value=1)
public class ServerRunner implements CommandLineRunner {

    @Autowired
    private SocketIOServerHandler socketIOServerHandler;

    @Override
    public void run(String... args) {
        socketIOServerHandler.start();
        System.out.println("socket.io服务端启动成功！");
    }

}
