package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysNoticeRecipient;

import java.util.List;

/**
 * 消息接收人Service接口
 * 
 * @author 韩
 * @date 2022-12-23
 */
public interface ISysNoticeRecipientService {
    /**
     * 查询消息接收人
     * 
     * @param id 消息接收人主键
     * @return 消息接收人
     */
    SysNoticeRecipient selectSysNoticeRecipientById(Long id);

    /**
     * 查询消息接收人列表
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 消息接收人集合
     */
    List<SysNoticeRecipient> selectSysNoticeRecipientList(SysNoticeRecipient sysNoticeRecipient);

    /**
     * 新增消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    int insertSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient);

    /**
     * 修改消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    int updateSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient);

    /**
     * 批量删除消息接收人
     * 
     * @param ids 需要删除的消息接收人主键集合
     * @return 结果
     */
    int deleteSysNoticeRecipientByIds(Long[] ids);

    /**
     * 删除消息接收人信息
     * 
     * @param id 消息接收人主键
     * @return 结果
     */
    int deleteSysNoticeRecipientById(Long id);
}
