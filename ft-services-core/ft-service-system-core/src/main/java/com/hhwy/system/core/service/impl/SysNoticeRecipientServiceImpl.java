package com.hhwy.system.core.service.impl;

import com.hhwy.system.core.domain.SysNoticeRecipient;
import com.hhwy.system.core.mapper.SysNoticeRecipientMapper;
import com.hhwy.system.core.service.ISysNoticeRecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息接收人Service业务层处理
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Service
public class SysNoticeRecipientServiceImpl implements ISysNoticeRecipientService {

    @Autowired
    private SysNoticeRecipientMapper sysNoticeRecipientMapper;

    /**
     * 查询消息接收人
     * 
     * @param id 消息接收人主键
     * @return 消息接收人
     */
    @Override
    public SysNoticeRecipient selectSysNoticeRecipientById(Long id) {
        return sysNoticeRecipientMapper.selectSysNoticeRecipientById(id);
    }

    /**
     * 查询消息接收人列表
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 消息接收人
     */
    @Override
    public List<SysNoticeRecipient> selectSysNoticeRecipientList(SysNoticeRecipient sysNoticeRecipient) {
        return sysNoticeRecipientMapper.selectSysNoticeRecipientList(sysNoticeRecipient);
    }

    /**
     * 新增消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    @Override
    public int insertSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient) {
        return sysNoticeRecipientMapper.insertSysNoticeRecipient(sysNoticeRecipient);
    }

    /**
     * 修改消息接收人
     * 
     * @param sysNoticeRecipient 消息接收人
     * @return 结果
     */
    @Override
    public int updateSysNoticeRecipient(SysNoticeRecipient sysNoticeRecipient) {
        return sysNoticeRecipientMapper.updateSysNoticeRecipient(sysNoticeRecipient);
    }



    /**
     * 批量删除消息接收人
     * 
     * @param ids 需要删除的消息接收人主键
     * @return 结果
     */
    @Override
    public int deleteSysNoticeRecipientByIds(Long[] ids) {
        return sysNoticeRecipientMapper.deleteSysNoticeRecipientByIds(ids);
    }

    /**
     * 删除消息接收人信息
     * 
     * @param id 消息接收人主键
     * @return 结果
     */
    @Override
    public int deleteSysNoticeRecipientById(Long id) {
        return sysNoticeRecipientMapper.deleteSysNoticeRecipientById(id);
    }
}
