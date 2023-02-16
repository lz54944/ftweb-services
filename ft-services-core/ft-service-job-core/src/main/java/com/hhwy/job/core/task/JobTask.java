package com.hhwy.job.core.task;

import com.hhwy.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author hhwy
 */
@Component("jobTask")
public class JobTask {
    public void jobMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void jobParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void jobNoParams() {
        System.out.println("执行无参方法");
    }
}
