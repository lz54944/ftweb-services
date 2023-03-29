package com.hhwy.system.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息公告查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformNoticeQueryVo {

    //当前登录人工号
    private Long userId;

    //消息标题
    private String noticeTitle;

    //消息类型
    private String noticeType;

    //租户标识
    private String tenantKey;

    //读取状态（0：未读，1：已读）
    private String readStatus;
}
