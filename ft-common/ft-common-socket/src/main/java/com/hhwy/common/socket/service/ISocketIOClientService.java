package com.hhwy.common.socket.service;

import io.socket.client.Socket;

public interface ISocketIOClientService {

    void publishEvents(Socket socket);

    void emitEvents(Socket socket);
}
