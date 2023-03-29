package com.hhwy.system.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息接收人对象 sys_notice_recipient
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysNoticeRecipient {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 公告ID */
    private Long noticeId;

    /** 读取状态 */
    private String readStatus;

    /** 消息接收人id */
    private Long recipientId;

    /** 消息接收人 */
    private String recipientName;

    //备注
    private String remark;
}
