package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysNoticeRecipient;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息接收人Mapper接口
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Repository
public interface SysNoticeRecipientMapper {
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
     * 根据noticeId查询接收人集合
     * @param noticeId
     * @return
     */
    List<SysNoticeRecipient> getSysNoticeRecipientListByNoticeId(@Param("noticeId") Long noticeId);

    /**
     * 新增消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    int insertSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient);

    /**
     * 批量插入
     * @param sysNoticeRecipientList
     */
    void insertSysNoticeRecipientList(@Param("recipientList") List<SysNoticeRecipient> sysNoticeRecipientList);

    /**
     * 修改消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    int updateSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient);

    /**
     * 根据消息id和消息接收人id修改消息读取状态为已读
     * @param noticeId
     * @param recipientId
     * @return
     */
    int updateReadStatusByNoticeIdAndRecipientId(@Param("noticeId") Long noticeId,@Param("recipientId") Long recipientId);

    /**
     * 删除消息接收人
     * 
     * @param id 消息接收人主键
     * @return 结果
     */
    int deleteSysNoticeRecipientById(Long id);

    /**
     * 批量删除消息接收人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysNoticeRecipientByIds(Long[] ids);

    /**
     * 根据noticeId删除接收人
     * @param noticeId
     */
    void deleteSysNoticeRecipientByNoticeId(@Param("noticeId") Long noticeId);

    /**
     * 根据noticeIds批量删除接收人
     * @param noticeIds
     */
    void deleteSysNoticeRecipientByNoticeIds(@Param("noticeIds") Long[] noticeIds);

}
