package com.hhwy.auth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.hhwy.common.security.annotation.EnableFtFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 认证授权中心
 *
 * @author hhwy
 */
@EnableFtFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FtAuthApplication {
    public static void main(String[] args) throws UnknownHostException {
        Logger logger = LoggerFactory.getLogger(FtAuthApplication.class);
        ConfigurableApplicationContext application = SpringApplication.run(FtAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  授权模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'               \n" +
                "(♥◠‿◠)ﾉﾞ  授权模块启动成功   ლ(´ڡ`ლ)ﾞ");
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        logger.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
                "本地访问网址: \t\thttp://127.0.0.1:" + port + path + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n" +
                "----------------------------------------------------------");
    }
}
