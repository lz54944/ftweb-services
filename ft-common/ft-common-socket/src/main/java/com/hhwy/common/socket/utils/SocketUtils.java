package com.hhwy.common.socket.utils;

import com.corundumstudio.socketio.SocketIOClient;
import org.apache.commons.collections4.CollectionUtils;
import java.util.List;
import java.util.Map;

public class SocketUtils {

    /**
     * 获取客户端url中的clientId参数（这里根据个人需求和客户端对应修改即可）
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    public static String getParamsByClient(SocketIOClient client) {
        // 获取客户端url参数（这里的clientId是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> clientIdList = params.get("clientId");
        if (!CollectionUtils.isEmpty(clientIdList)) {
            return clientIdList.get(0);
        }
        return null;
    }

    /**
     * 获取连接的客户端ip地址
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    public static String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }

}
