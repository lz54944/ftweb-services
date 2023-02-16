package com.hhwy.common.socket.service;

/**
 * 案例
 */
public class SocketIOServerServiceAdapter implements ISocketIOServerService {

    @Override
    public void addEventListeners() {}

    /**
     * <br>方法描述：推送信息给指定客户端
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/17 16:27
     * <br>备注：无
     */
    @Override
    public void pushMessageToClient(String eventName, String clientId, Object ... data) {}

    /**
     * <br>方法描述：广播信息
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/17 16:27
     * <br>备注：默认是向所有的socket连接进行广播，但是不包括发送者自身，如果自己也打算接收消息的话，需要给自己单独发送
     */
    @Override
    public void broadcastMessage(String eventName, Object ... data) {}

}
