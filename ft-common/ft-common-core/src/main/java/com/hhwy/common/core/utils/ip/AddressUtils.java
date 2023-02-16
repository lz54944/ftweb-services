package com.hhwy.common.core.utils.ip;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.http.HttpUtils;
import com.hhwy.common.core.utils.json.JSON;
import com.hhwy.common.core.utils.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取地址类
 * 
 * @author jzq
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    private static final boolean addressEnabled = true;

    public static String getRealAddressByIP(String ip) {
        String address = "-";

        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (addressEnabled) {
            try {
                Map<String,String> param = new HashMap<>();
                param.put("ip",ip);
                String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return address;
                }
                JSONObject obj = JSON.unmarshal(rspStr, JSONObject.class);
                JSONObject data = obj.getObj("data");
                String region = data.getStr("region");
                String city = data.getStr("city");
                address = region + " " + city;
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return address;
    }

    public static String getServerIP(){
        String ip = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
