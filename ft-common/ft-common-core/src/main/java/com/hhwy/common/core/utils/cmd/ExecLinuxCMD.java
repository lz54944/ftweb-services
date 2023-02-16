package com.hhwy.common.core.utils.cmd;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.UUIDUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * java在linux环境下执行linux命令，然后返回命令返回值。
 *
 * @author jzq
 */
public class ExecLinuxCMD {

    private static Map<String, Queue<String>> logQueueMap = new HashMap<>();

     /**
        * 获取cmd执行的log日志
        */
    public static String popExeLog(String logId) {
        Queue<String> queue = logQueueMap.get(logId);
        if (queue != null) {
            if (queue.size() > 0){
                return queue.poll(); //返回第一个元素，并在队列中删除
            }else {
                return "";
            }
        }else {
            return null;
        }
    }

    public static void clearExeLog(String logId) {
        logQueueMap.remove(logId);
    }

    public static Object exeCmd(String cmd) {
        return exeCmd(cmd, null, null);
    }

    public static Object exeCmd(String cmd, String logId) {
        return exeCmd(cmd, null, logId);
    }

    public static Object exeCmd(String cmd, Channel channel) {
        return exeCmd(cmd, channel, null);
    }

    public static void exeCmd(String cmd, Queue<String> logQueue) {
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (logQueue != null) {
                    //添加元素
                    logQueue.offer(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object exeCmd(String cmd, Channel channel, String logId) {
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            Queue<String> queue = null;
            if (StringUtils.isNotEmpty(logId)) {
                queue = new LinkedList<>();
                logQueueMap.put(logId, queue);
            }
            while ((line = br.readLine()) != null) {
                if (queue != null) {
                    //添加元素
                    queue.offer(line);
                }
                sb.append(line).append("\n");
                if (channel != null) {
                    channel.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now() + " [" + Thread.currentThread().getName() + "]" + line));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String pwdString = exeCmd("pwd", null, UUIDUtils.getShortUuid()).toString();
        System.out.println(pwdString);

        /*String netsString = exeCmd("netstat -nat|grep -i \"80\"|wc -l").toString();
        System.out.println(netsString);*/
    }

}
