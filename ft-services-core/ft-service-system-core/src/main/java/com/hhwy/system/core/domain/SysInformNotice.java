package com.hhwy.system.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 通知公告对象 sys_inform_notice
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysInformNotice {
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Long noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告类型（1通知 2公告 3待办） */
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    private String status;

    /** 消息发送人id */
    private Long senderId;

    /** 消息发送人 */
    private String senderName;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 相关链接 */
    private String correlationLink;

    /** 创建者 */
    private String createUser;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateUser;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //备注
    private String remark;

    /** 租户标识 */
    private String tenantKey;

    //接收人集合
    private List<SysNoticeRecipient> recipientList;
}
