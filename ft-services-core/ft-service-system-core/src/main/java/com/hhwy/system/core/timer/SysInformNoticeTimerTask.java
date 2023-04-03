package com.hhwy.system.core.timer;

import com.hhwy.system.core.mapper.SysInformNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息定时任务
 */
@Component
public class SysInformNoticeTimerTask {

    @Autowired
    private SysInformNoticeMapper sysInformNoticeMapper;

    /**
     * 定时任务，关闭结束时间到期的所有任务
     */
    @Scheduled(cron="* 1 0 1/1 * ? ") //每天凌晨 00:01 执行
    public void closeOutOfDateNotice(){
        //获取今天的日期
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //关闭结束时间在指定日期之前所有的消息
        sysInformNoticeMapper.closeOutOfDateNotice(nowDate);
    }
}
