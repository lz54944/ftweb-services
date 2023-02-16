package com.hhwy.common.socket.annotation;

import com.hhwy.common.socket.config.SocketIOServerConfig;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * <br>描 述： EnableFtSocket
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/31 17:21
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SocketIOServerConfig.class)
public @interface EnableFtSocketServer {
}
