package com.hhwy.common.core.utils.cmd;

import com.hhwy.common.core.exception.CustomException;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.os.Os;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * java在window环境下执行window命令，然后返回命令返回值。
 *
 * @author jzq
 */
public class ExecWindowCMD {
    private static Map<String, Queue<String>> logQueueMap = new HashMap<>();

    /**
     * 获取cmd执行的log日志
     */
    public static String popExeLog(String logId) {
        Queue<String> queue = logQueueMap.get(logId);
        if (queue != null) {
            if (queue.size() > 0) {
                return queue.poll(); //返回第一个元素，并在队列中删除
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    public static void clearExeLog(String logId) {
        logQueueMap.remove(logId);
    }

    /**
     * <br>方法描述：执行cmd命令
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/3 13:50
     * <br>备注：执行多条命令  d: && cd D:\office\gradle-6.0.1
     */

    public static String exeCmd(String cmd) {
        return exeCmd(cmd, null, null);
    }

    public static String exeCmd(String cmd, String logId) {
        return exeCmd(cmd, null, logId);
    }

    public static String exeCmd(String cmd, Channel channel) {
        return exeCmd(cmd, channel, null);
    }

    public static void exeCmd(String cmd, Queue<String> logQueue) {
        if (!"Windows".equals(Os.getOSName())) {
            throw new CustomException("当前操作系统非Window");
        }
        String commandStr = "cmd exe /c " + cmd;
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line;
            while ((line = br.readLine()) != null) {
                if (logQueue != null) {
                    //添加元素
                    logQueue.offer(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("命令执行失败！");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String exeCmd(String cmd, Channel channel, String logId) {
        if (!"Windows".equals(Os.getOSName())) {
            throw new CustomException("当前操作系统非Window");
        }
        String commandStr = "cmd exe /c " + cmd;
        BufferedReader br = null;
        Queue<String> queue = null;
        if (StringUtils.isNotEmpty(logId)) {
            queue = new LinkedList<>();
            logQueueMap.put(logId, queue);
        }
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (queue != null) {
                    //添加元素
                    queue.offer(line);
                }
                if (channel != null) {
                    channel.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now() + " [" + Thread.currentThread().getName() + "]" + line));
                }
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("命令执行失败！");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <br>方法描述：执行bat脚本
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/9/3 13:49
     * <br>备注：无
     */
    public static int exeBat(String path) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process exec = rt.exec(path);
            // 进程的出口值。根据惯例，0 表示正常终止。
            int result = exec.waitFor();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("脚本执行失败！");
        }
    }

    //备份test test2两个库
    public static void mysqlDumpTest() {
        // -d 不导出数据
        //String commandStr = "D:\\office\\mysql-5.6.24-win32\\bin\\mysqldump -h192.168.1.66 -P3306 --default-character-set utf8 -uroot -proot -d  --add-drop-database  --databases test test2> C:\\Users\\hhwy-jzq\\Desktop\\test.sql";

        String commandStr = whereMysql() + "mysqldump -h192.168.1.66 -P3306 --default-character-set utf8 -uroot -proot --add-drop-database  --databases test test2> C:\\Users\\hhwy-jzq\\Desktop\\test.sql";
        ExecWindowCMD.exeCmd(commandStr);
    }

    //通过执行SQL脚本 恢复库
    public static void importMysqlDumpTest() {
        //String commandStr = "D:\\office\\mysql-5.6.24-win32\\bin\\mysql  -h192.168.1.66 -P3306 --default-character-set utf8 -uroot -proot < C:\\Users\\hhwy-jzq\\Desktop\\test.sql";
        String commandStr = whereMysql() + "\\bin\\mysql  -h192.168.1.66 -P3306 --default-character-set utf8 -uroot -proot < C:\\Users\\hhwy-jzq\\Desktop\\test.sql";
        ExecWindowCMD.exeCmd(commandStr);
    }

    public static String whereMysql() {
        String commandStr = "where mysqld";
        String result = ExecWindowCMD.exeCmd(commandStr);
        if (StringUtils.isNotEmpty(result)) {
            result = result.substring(0, result.length() - "mysqld.exe".length() - 6);
        } else {
            throw new CustomException("未检测到MySQL！");
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
       /* String commandStr = "ping www.taobao.com";
        String result = exeCmd(commandStr);
        System.err.println(result);*/

        mysqlDumpTest();

        //importMysqlDumpTest();

        /*String batPath = "C:\\Users\\hhwy-jzq\\Desktop\\test.bat";
        exeBat(batPath);*/

        /*String commandStr = "e: && cd E:\\office\\workspace\\office\\FtWeb\\ftweb-ui && yarn build";
        new Thread(() -> {
            exeCmd(commandStr, "11");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearExeLog("11");
        }).start();

        Thread.sleep(1000);
        while (true) {
            String s = popExeLog("11");
            if (s == null) {
                break;
            }
            if (s != "") {
                System.err.println(s);
            }
        }*/
    }
}