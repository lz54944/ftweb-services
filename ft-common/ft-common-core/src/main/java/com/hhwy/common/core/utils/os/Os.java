package com.hhwy.common.core.utils.os;

import java.util.regex.Pattern;

public class Os {

     /**
        * <br>方法描述：获取操作系统名称
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/9/3 14:02
        * <br>备注：无
        */
    public static String getOSName(){
        String osName = System.getProperty("os.name");
        if(Pattern.matches("Windows.*",osName)) {
            return "Windows";
        } else if(Pattern.matches("Linux.*",osName)) {
            return "Linux";
        } else if (Pattern.matches("Mac.*",osName)) {
            return "Mac";
        } else {
            return "osName";
        }
    }

}
