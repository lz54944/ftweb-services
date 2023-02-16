package com.hhwy.demo;

import com.hhwy.common.security.annotation.EnableFtFeignClients;
import com.hhwy.common.socket.annotation.EnableFtSocketClient;
import com.hhwy.common.tenant.annotation.EnableFtTenantClient;
import com.hhwy.demo.test.MyImportBeanDefinition;
import com.hhwy.demo.test.ServiceImportSelector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hhwy.common.security.annotation.EnableCustomConfig;
import com.hhwy.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Demo模块
 *
 * @author hhwy
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableFtFeignClients
@SpringBootApplication
@EnableFtTenantClient
@EnableFtSocketClient
@Import(MyImportBeanDefinition.class)
public class FtDemoApplication {
    public static void main(String[] args) throws UnknownHostException {
        Logger logger = LoggerFactory.getLogger(FtDemoApplication.class);
        ConfigurableApplicationContext application = SpringApplication.run(FtDemoApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Demo模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'               \n" +
                "(♥◠‿◠)ﾉﾞ  Demo模块启动成功   ლ(´ڡ`ლ)ﾞ");
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
