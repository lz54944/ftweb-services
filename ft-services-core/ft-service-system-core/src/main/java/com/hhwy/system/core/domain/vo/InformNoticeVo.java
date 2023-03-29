package com.hhwy.system.core.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 前台消息返回实体
 */
@Data
public class InformNoticeVo {

    private String key;

    private String name;

    private List<InformNotice> list;
}
