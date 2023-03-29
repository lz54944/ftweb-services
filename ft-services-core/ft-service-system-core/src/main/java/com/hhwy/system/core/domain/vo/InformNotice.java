package com.hhwy.system.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InformNotice {

    /** 公告ID */
    private Long id;

    /** 公告标题 */
    private String title;

    /** 公告类型（1通知 2公告 3待办） */
    private String type;

    /** 公告内容 */
    private String description;

    /** 消息发送人id */
    private String senderId;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datetime;

    //备注
    private String remark;

    /** 读取状态 */
    private String clickClose;
}
